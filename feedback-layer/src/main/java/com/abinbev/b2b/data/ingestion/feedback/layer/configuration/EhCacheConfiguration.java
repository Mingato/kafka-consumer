package com.abinbev.b2b.data.ingestion.feedback.layer.configuration;

import com.abinbev.b2b.IngestionTrackingRecord;
import com.abinbev.b2b.data.ingestion.feedback.layer.configuration.properties.CacheProperties;
import java.time.Duration;
import java.util.function.Supplier;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.ExpiryPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EhCacheConfiguration {

  private final CacheProperties cacheProperties;

  @Autowired
  public EhCacheConfiguration(
      @Qualifier("feedbackLayerCacheProperties") CacheProperties cacheProperties) {
    this.cacheProperties = cacheProperties;
  }

  @Bean
  public Cache<String, IngestionTrackingRecord> getCache() {
    return getCacheManager()
        .createCache(
            CacheProperties.CACHE_NAME,
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                    String.class,
                    IngestionTrackingRecord.class,
                    ResourcePoolsBuilder.heap(cacheProperties.getHeapSize()))
                .withExpiry(getExpiryPolicy())
                .build());
  }

  private CacheManager getCacheManager() {
    return CacheManagerBuilder.newCacheManagerBuilder().build(true);
  }

  private ExpiryPolicy<String, IngestionTrackingRecord> getExpiryPolicy() {
    return new ExpiryPolicy<String, IngestionTrackingRecord>() {
      @Override
      public Duration getExpiryForCreation(
          String s, IngestionTrackingRecord ingestionTrackingRecord) {
        return Duration.ofSeconds(cacheProperties.getTtlSeconds());
      }

      @Override
      public Duration getExpiryForAccess(
          String s, Supplier<? extends IngestionTrackingRecord> supplier) {
        return null;
      }

      @Override
      public Duration getExpiryForUpdate(
          String s,
          Supplier<? extends IngestionTrackingRecord> supplier,
          IngestionTrackingRecord ingestionTrackingRecord) {
        return null;
      }
    };
  }
}
