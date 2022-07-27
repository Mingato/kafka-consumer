package com.abinbev.b2b.data.ingestion.feedback.layer.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration("feedbackLayerKafkaProperties")
@ConfigurationProperties("spring.kafka")
public class KafkaProperties {

  private String feedBackTopic;

  public String getFeedBackTopic() {
    return feedBackTopic;
  }

  public void setFeedBackTopic(final String feedBackTopic) {
    this.feedBackTopic = feedBackTopic;
  }
}
