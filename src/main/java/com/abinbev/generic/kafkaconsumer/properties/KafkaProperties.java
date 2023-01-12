package com.abinbev.generic.kafkaconsumer.properties;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties("spring.kafka")
@Data
public class KafkaProperties {

    @NotEmpty
    private String vendorIds;

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

    @SneakyThrows
    public List<String> getVendorIds(){

        Map<String, String> vendorIdCountryMap = (new ObjectMapper()).readValue(vendorIds, HashMap.class);

        return new ArrayList<>(vendorIdCountryMap.keySet());
    }

    @SneakyThrows
    public List<String> getCountries(){

        Map<String, String> vendorIdCountryMap = (new ObjectMapper()).readValue(vendorIds, HashMap.class);

        return new ArrayList<>(vendorIdCountryMap.values());
    }

}