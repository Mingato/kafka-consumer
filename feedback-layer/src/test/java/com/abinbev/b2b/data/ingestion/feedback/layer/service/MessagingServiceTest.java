package com.abinbev.b2b.data.ingestion.feedback.layer.service;

import static com.abinbev.b2b.data.ingestion.feedback.layer.util.TestDataFactory.INGESTION_TRACKING_RECORD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.abinbev.b2b.IngestionTrackingRecord;
import com.abinbev.b2b.data.ingestion.feedback.layer.configuration.properties.KafkaProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
public class MessagingServiceTest {

  @Mock KafkaTemplate kafkaTemplate;

  private final KafkaProperties kafkaProperties = new KafkaProperties();

  @Test
  public void testMessagingService() {
    kafkaProperties.setFeedBackTopic("TEST_TOPIC");
    ArgumentCaptor<String> traceIdArgumentCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> topicArgumentCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<IngestionTrackingRecord> recordArgumentCaptor =
        ArgumentCaptor.forClass(IngestionTrackingRecord.class);
    MessagingService messagingService = new MessagingService(kafkaTemplate, kafkaProperties);
    messagingService.send(INGESTION_TRACKING_RECORD.getTraceId(), INGESTION_TRACKING_RECORD);
    verify(kafkaTemplate, times(1))
        .send(
            topicArgumentCaptor.capture(),
            traceIdArgumentCaptor.capture(),
            recordArgumentCaptor.capture());
    assertEquals(kafkaProperties.getFeedBackTopic(), topicArgumentCaptor.getValue());
    assertEquals(INGESTION_TRACKING_RECORD.getTraceId(), traceIdArgumentCaptor.getValue());
    assertEquals(INGESTION_TRACKING_RECORD, recordArgumentCaptor.getValue());
  }
}
