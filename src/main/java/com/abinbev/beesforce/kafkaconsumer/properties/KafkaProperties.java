package com.abinbev.beesforce.kafkaconsumer.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("spring.kafka")
@Data
public class KafkaProperties {

    private long retry;

    private String topicPrefix;
    private String topicPrefixProcessed;

    private String dltSuffix;
    private String retrySuffix;
    private int partitions;
    private String feedBackTopic;
    private int feedBackPartitions;
    private int partitionsProcessed;
    private int dltPartitions;
    private int dltPartitionsProcessed;


}