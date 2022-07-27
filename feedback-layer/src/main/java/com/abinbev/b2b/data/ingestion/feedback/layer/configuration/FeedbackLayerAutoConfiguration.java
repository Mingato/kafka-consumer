package com.abinbev.b2b.data.ingestion.feedback.layer.configuration;

import com.abinbev.b2b.IngestionTrackingRecord;
import com.abinbev.b2b.data.ingestion.feedback.layer.service.FeedbackLayerService;
import com.abinbev.b2b.data.ingestion.feedback.layer.service.MessagingService;
import org.ehcache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.abinbev.b2b.data.ingestion.feedback.layer")
public class FeedbackLayerAutoConfiguration {

  private final Cache<String, IngestionTrackingRecord> recordCache;

  private final MessagingService messagingService;

  @Autowired
  public FeedbackLayerAutoConfiguration(
      Cache<String, IngestionTrackingRecord> recordCache,
      @Qualifier("feedbackLayerMessagingService") MessagingService messagingService) {
    this.recordCache = recordCache;
    this.messagingService = messagingService;
  }

  @Bean
  @ConditionalOnMissingBean({FeedbackLayerService.class})
  public FeedbackLayerService getFeedbackLayerService() {
    return new FeedbackLayerService(recordCache, messagingService);
  }
}
