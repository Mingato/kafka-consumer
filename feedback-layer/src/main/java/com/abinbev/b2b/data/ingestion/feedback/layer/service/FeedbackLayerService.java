package com.abinbev.b2b.data.ingestion.feedback.layer.service;

import static java.util.Optional.ofNullable;

import com.abinbev.b2b.IngestionTrackingRecord;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;
import org.ehcache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeedbackLayerService {

  private static final String UNKNOWN = "unknown";
  private static final Logger LOGGER = LoggerFactory.getLogger(FeedbackLayerService.class);

  private final Cache<String, IngestionTrackingRecord> recordCache;

  private final MessagingService messagingService;

  public FeedbackLayerService(
      Cache<String, IngestionTrackingRecord> recordCache, MessagingService messagingService) {
    this.recordCache = recordCache;
    this.messagingService = messagingService;
  }

  public void createTrackingRecord(
      String parentTraceId,
      String traceId,
      String entityOperation,
      String entityVersion,
      String entityName,
      String country,
      String vendorId,
      String sourceSystem) {
    if (recordCache.containsKey(traceId)) {
      LOGGER.info(
          "The traceId '{}' is already in use, update the existing one or use a different traceId.",
          traceId);
    } else {
      long timestamp = Instant.now().toEpochMilli();
      IngestionTrackingRecord record = new IngestionTrackingRecord();
      record.setId(UUID.randomUUID().toString());
      record.setParentTraceId(parentTraceId);
      record.setTraceId(traceId);
      record.setOperation(getParseOperationName(entityOperation, entityVersion, entityName));
      record.setSuccess(false);
      record.setSteps(new ArrayList<>());
      record.setCountry(country);
      record.setVendorId(vendorId);
      record.setSourceSystem(sourceSystem);
      record.setCreatedAt(timestamp);
      record.setDurationMs(timestamp);
      recordCache.put(traceId, record);
    }
  }

  private String getParseOperationName(
      String entityOperation, String entityVersion, String entityName) {
    return getUnknownWhenNull(entityName)
        + "."
        + getUnknownWhenNull(entityVersion)
        + "."
        + getUnknownWhenNull(entityOperation);
  }

  private String getUnknownWhenNull(String value) {
    return value == null || value.isEmpty() ? UNKNOWN : value.toUpperCase();
  }

  public void setSuccess(String traceId) {
    ofNullable(recordCache.get(traceId)).ifPresent(record -> record.setSuccess(true));
  }

  public void setFailure(String traceId) {
    ofNullable(recordCache.get(traceId)).ifPresent(record -> record.setSuccess(false));
  }

  public void addStep(String traceId, String step) {
    ofNullable(recordCache.get(traceId)).ifPresent(record -> record.getSteps().add(step));
  }

  public void setPayload(String traceId, String payload) {
    ofNullable(recordCache.get(traceId)).ifPresent(record -> record.setPayload(payload));
  }

  public void setErrorMessage(String traceId, String errorMessage) {
    ofNullable(recordCache.get(traceId)).ifPresent(record -> record.setErrorMessage(errorMessage));
  }

  public void setErrorCode(String traceId, String errorCode) {
    ofNullable(recordCache.get(traceId)).ifPresent(record -> record.setErrorMessage(errorCode));
  }

  public void publish(String traceId) {
    ofNullable(recordCache.get(traceId))
        .ifPresent(
            record -> {
              try {
                ofNullable(record.getDurationMs())
                    .ifPresentOrElse(
                        duration -> record.setDurationMs(Instant.now().toEpochMilli() - duration),
                        () -> record.setDurationMs(0L));
                messagingService.send(traceId, record);
              } catch (Exception e) {
                LOGGER.warn("Failed to publish feedback layer event.", e);
              } finally {
                recordCache.remove(traceId);
              }
            });
  }
}
