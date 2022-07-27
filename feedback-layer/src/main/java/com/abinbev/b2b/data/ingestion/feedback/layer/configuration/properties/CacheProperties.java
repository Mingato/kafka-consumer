package com.abinbev.b2b.data.ingestion.feedback.layer.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration("feedbackLayerCacheProperties")
@ConfigurationProperties("spring.feedback.layer.cache")
public class CacheProperties {

  private long heapSize;

  private long ttlSeconds;

  public static final String CACHE_NAME = "records";

  public long getHeapSize() {
    return heapSize;
  }

  public void setHeapSize(long heapSize) {
    this.heapSize = heapSize;
  }

  public long getTtlSeconds() {
    return ttlSeconds;
  }

  public void setTtlSeconds(long ttlSeconds) {
    this.ttlSeconds = ttlSeconds;
  }
}
