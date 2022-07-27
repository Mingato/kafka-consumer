package com.abinbev.b2b.data.ingestion.feedback.layer.service;

import com.abinbev.b2b.IngestionTrackingRecord;
import com.abinbev.b2b.data.ingestion.feedback.layer.configuration.properties.KafkaProperties;
import com.abinbev.b2b.data.ingestion.feedback.layer.helper.TraceIdHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service("feedbackLayerMessagingService")
public class MessagingService {

  private final KafkaTemplate<String, IngestionTrackingRecord> kafkaTemplate;
  private final KafkaProperties properties;

  @Autowired
  public MessagingService(
      final KafkaTemplate<String, IngestionTrackingRecord> kafkaTemplate,
      @Qualifier("feedbackLayerKafkaProperties") final KafkaProperties properties) {
    this.kafkaTemplate = kafkaTemplate;
    this.properties = properties;
  }

  public void send(
      final String requestTraceId, final IngestionTrackingRecord ingestionTrackingRecord) {
    String traceId = TraceIdHelper.getTraceIdFromChunk(requestTraceId);
    kafkaTemplate.send(properties.getFeedBackTopic(), traceId, ingestionTrackingRecord);
  }
}
