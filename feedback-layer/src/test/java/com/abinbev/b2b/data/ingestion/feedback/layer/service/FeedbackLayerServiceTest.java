package com.abinbev.b2b.data.ingestion.feedback.layer.service;

import static com.abinbev.b2b.data.ingestion.feedback.layer.util.TestDataFactory.INGESTION_TRACKING_RECORD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.abinbev.b2b.IngestionTrackingRecord;
import com.abinbev.b2b.data.ingestion.feedback.layer.configuration.EhCacheConfiguration;
import com.abinbev.b2b.data.ingestion.feedback.layer.configuration.properties.CacheProperties;
import com.abinbev.b2b.data.ingestion.feedback.layer.util.MemoryAppender;
import org.ehcache.Cache;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

@ExtendWith(MockitoExtension.class)
public class FeedbackLayerServiceTest {

  private static final String LOG_MSG_TEMPALTE =
      "The traceId '%s' is already in use, update the existing one or use a different traceId.";

  private static final String LOG_MSG_PUB_FAILURE = "Failed to publish feedback layer event.";

  @Mock private MessagingService messagingService;

  private final CacheProperties cacheProperties = new CacheProperties();

  private final EhCacheConfiguration ehCacheConfiguration =
      new EhCacheConfiguration(cacheProperties);

  @Test
  public void feedbackLayerServiceTest() {
    cacheProperties.setHeapSize(5);
    cacheProperties.setTtlSeconds(2);
    Cache<String, IngestionTrackingRecord> recordsCache = ehCacheConfiguration.getCache();
    ArgumentCaptor<IngestionTrackingRecord> recordArgumentCaptor =
        ArgumentCaptor.forClass(IngestionTrackingRecord.class);
    ArgumentCaptor<String> traceIdArgumentCaptor = ArgumentCaptor.forClass(String.class);
    FeedbackLayerService feedbackLayerService =
        new FeedbackLayerService(recordsCache, messagingService);
    feedbackLayerService.createTrackingRecord(
        INGESTION_TRACKING_RECORD.getParentTraceId(),
        INGESTION_TRACKING_RECORD.getTraceId(),
        "POST",
        "V2",
        "ORDERS",
        INGESTION_TRACKING_RECORD.getCountry(),
        INGESTION_TRACKING_RECORD.getVendorId(),
        INGESTION_TRACKING_RECORD.getSourceSystem());
    feedbackLayerService.setFailure(INGESTION_TRACKING_RECORD.getTraceId());
    IngestionTrackingRecord expectedRecord =
        recordsCache.get(INGESTION_TRACKING_RECORD.getTraceId());
    feedbackLayerService.publish(INGESTION_TRACKING_RECORD.getTraceId());
    verify(messagingService, times(1))
        .send(traceIdArgumentCaptor.capture(), recordArgumentCaptor.capture());
    assertEquals(INGESTION_TRACKING_RECORD.getTraceId(), traceIdArgumentCaptor.getValue());
    assertEquals(expectedRecord, recordArgumentCaptor.getValue());
    assertNull(recordsCache.get(INGESTION_TRACKING_RECORD.getTraceId()));
  }

  @Test
  public void feedbackLayerServiceTTLValidationTest() throws InterruptedException {
    cacheProperties.setHeapSize(5);
    cacheProperties.setTtlSeconds(2);
    Cache<String, IngestionTrackingRecord> recordsCache = ehCacheConfiguration.getCache();
    FeedbackLayerService feedbackLayerService =
        new FeedbackLayerService(recordsCache, messagingService);
    feedbackLayerService.createTrackingRecord(
        INGESTION_TRACKING_RECORD.getParentTraceId(),
        INGESTION_TRACKING_RECORD.getTraceId(),
        "POST",
        "V2",
        "ORDERS",
        INGESTION_TRACKING_RECORD.getCountry(),
        INGESTION_TRACKING_RECORD.getVendorId(),
        INGESTION_TRACKING_RECORD.getSourceSystem());
    feedbackLayerService.publish(INGESTION_TRACKING_RECORD.getTraceId());
    Thread.sleep(2000);
    assertNull(recordsCache.get(INGESTION_TRACKING_RECORD.getTraceId()));
  }

  @Test
  public void feedbackLayerServiceTraceIdAlreadyInUseTest() {
    cacheProperties.setHeapSize(5);
    cacheProperties.setTtlSeconds(2);
    MemoryAppender memoryAppender = getMemoryAppender(FeedbackLayerService.class);
    FeedbackLayerService feedbackLayerService =
        new FeedbackLayerService(ehCacheConfiguration.getCache(), messagingService);
    feedbackLayerService.createTrackingRecord(
        INGESTION_TRACKING_RECORD.getParentTraceId(),
        INGESTION_TRACKING_RECORD.getTraceId(),
        "POST",
        "V2",
        "ORDERS",
        INGESTION_TRACKING_RECORD.getCountry(),
        INGESTION_TRACKING_RECORD.getVendorId(),
        INGESTION_TRACKING_RECORD.getSourceSystem());
    feedbackLayerService.createTrackingRecord(
        INGESTION_TRACKING_RECORD.getParentTraceId(),
        INGESTION_TRACKING_RECORD.getTraceId(),
        "POST",
        "V2",
        "ORDERS",
        INGESTION_TRACKING_RECORD.getCountry(),
        INGESTION_TRACKING_RECORD.getVendorId(),
        INGESTION_TRACKING_RECORD.getSourceSystem());
    assertEquals(
        String.format(LOG_MSG_TEMPALTE, INGESTION_TRACKING_RECORD.getTraceId()),
        memoryAppender.getLoggedEvents().get(0).getFormattedMessage());
  }

  @Test
  public void feedbackLayerServicePublishFailureTest() {
    doThrow(new RuntimeException("Publish Failure")).when(messagingService).send(any(), any());
    cacheProperties.setHeapSize(5);
    cacheProperties.setTtlSeconds(2);
    MemoryAppender memoryAppender = getMemoryAppender(FeedbackLayerService.class);
    FeedbackLayerService feedbackLayerService =
        new FeedbackLayerService(ehCacheConfiguration.getCache(), messagingService);
    feedbackLayerService.createTrackingRecord(
        INGESTION_TRACKING_RECORD.getParentTraceId(),
        INGESTION_TRACKING_RECORD.getTraceId(),
        "POST",
        "V2",
        "ORDERS",
        INGESTION_TRACKING_RECORD.getCountry(),
        INGESTION_TRACKING_RECORD.getVendorId(),
        INGESTION_TRACKING_RECORD.getSourceSystem());
    feedbackLayerService.publish(INGESTION_TRACKING_RECORD.getTraceId());
    assertEquals(
        String.format(LOG_MSG_PUB_FAILURE, INGESTION_TRACKING_RECORD.getTraceId()),
        memoryAppender.getLoggedEvents().get(0).getFormattedMessage());
  }

  private MemoryAppender getMemoryAppender(Class clazz) {
    Logger logger = (Logger) LoggerFactory.getLogger(clazz);
    MemoryAppender memoryAppender = new MemoryAppender();
    memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
    logger.setLevel(Level.DEBUG);
    logger.addAppender(memoryAppender);
    memoryAppender.start();
    return memoryAppender;
  }
}
