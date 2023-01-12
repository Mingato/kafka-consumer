package com.abinbev.generic.kafkaconsumer.config;


import com.abinbev.generic.kafkaconsumer.exceptions.UnauthorizedRequest;
import com.abinbev.generic.kafkaconsumer.listener.GenericKafkaListener;
import com.abinbev.generic.kafkaconsumer.properties.KafkaProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Configuration
public class KafkaConfig {

    private final KafkaProperties kafkaProperties;
    private final GenericKafkaListener genericKafkaListener;

    @Value("${abi.toggle.countries}")
    private String countries;

    @Value("${abi.toggle.vendorIds}")
    private String vendorIds;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    private static final long INTERVAL = 0L;

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConfig.class);
    private static final int DLT_DESTINATION_PARTITION = -1;

    private final Collection<Class<? extends Exception>> notRetryableExceptions =
            Arrays.asList(
                    ConstraintViolationException.class,
                    UnsupportedOperationException.class,
                    UnrecognizedPropertyException.class,
                    JsonProcessingException.class,
                    UnauthorizedRequest.class);


    public KafkaConfig(final KafkaProperties kafkaProperties, GenericKafkaListener genericKafkaListener) {
        this.kafkaProperties = kafkaProperties;
        this.genericKafkaListener = genericKafkaListener;
    }

    @Bean
    public List<ConcurrentMessageListenerContainer<Object, Object>> kafkaListenerContainer(
            ConcurrentKafkaListenerContainerFactory<Object, Object> factory) {
        Properties kafkaConsumerProperties = new Properties();
        kafkaConsumerProperties.put("max.poll.records", 50);
        return getConsumerTopicNames().stream()
                .filter(name -> !name.endsWith(kafkaProperties.getDltSuffix()))
                .map(
                        topic -> {
                            ConcurrentMessageListenerContainer<Object, Object> container =
                                    factory.createContainer(topic);
                            container.getContainerProperties().setMessageListener(getListener(topic));
                            container.getContainerProperties().setGroupId(getGroupId(topic));
                            container
                                    .getContainerProperties()
                                    .setKafkaConsumerProperties(kafkaConsumerProperties);
                            container.start();
                            return container;
                        })
                .collect(Collectors.toList());
    }

    private String getGroupId(String topic) {
        boolean isRetry = topic.endsWith(kafkaProperties.getRetrySuffix());
        String topicRoot = topic.replace(kafkaProperties.getRetrySuffix(), "");
        String country = topicRoot.substring(topicRoot.length() - 3);
        return this.groupId
                + country
                + (isRetry ? "-" + kafkaProperties.getRetrySuffix().toLowerCase().replace(".", "") : "");
    }

    private MessageListener<String, Object> getListener(String topic) {
        return topic.endsWith(kafkaProperties.getRetrySuffix())
                ? genericKafkaListener::consumeRetry
                : genericKafkaListener::consume;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactory(
            ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
            ConsumerFactory<Object, Object> kafkaConsumerFactory,
            KafkaOperations<String, Object> template) {

        ConcurrentKafkaListenerContainerFactory<Object, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        configurer.configure(factory, kafkaConsumerFactory);
        SeekToCurrentErrorHandler handler =
                new SeekToCurrentErrorHandler(
                        new DeadLetterPublishingRecoverer(
                                template,
                                dltDestinationResolverBuilder(
                                        kafkaProperties.getRetrySuffix(), kafkaProperties.getDltSuffix())),
                        new FixedBackOff(INTERVAL, kafkaProperties.getRetry()));
        addNotRetryableExceptions(handler);
        factory.setErrorHandler(handler);
        return factory;
    }

    private void addNotRetryableExceptions(SeekToCurrentErrorHandler handler) {
        for (Class<? extends Exception> exception : notRetryableExceptions) {
            handler.addNotRetryableExceptions(exception);
        }
    }

    @Bean
    public KafkaAdmin.NewTopics createTopics() {
        List<NewTopic> newTopics = createTopicsList();
        NewTopic[] newTopicsArray = newTopics.toArray(new NewTopic[0]);
        return new KafkaAdmin.NewTopics(newTopicsArray);
    }

    public List<NewTopic> createTopicsList() {
        List<NewTopic> newTopics = new ArrayList<>();


            newTopics.addAll(
                createTopicListByPrefix(
                    kafkaProperties.getTopicPrefix(),
                    kafkaProperties.getPartitions(),
                    kafkaProperties.getDltPartitions(),
                    true,
                    Arrays.asList(vendorIds.split(","))));

            newTopics.addAll(
                createTopicListByPrefix(
                    kafkaProperties.getTopicPrefix(),
                    kafkaProperties.getPartitions(),
                    kafkaProperties.getDltPartitions(),
                    true,
                    Arrays.asList(countries.split(","))));


        newTopics.add(
                TopicBuilder.name(kafkaProperties.getFeedBackTopic())
                        .partitions(kafkaProperties.getFeedBackPartitions())
                        .build());

        return newTopics;
    }

    private List<String> topicNamesByPrefix(String topicPrefix, boolean withRetry) {

        return Stream.concat(
                Arrays.asList(countries.split(",")).stream(), Arrays.asList(vendorIds.split(",")).stream())
                .flatMap(
                        country -> {
                            HashSet<String> topics = new HashSet<>();
                            String baseName = (topicPrefix + "-" + country).toLowerCase();
                            topics.add(baseName);
                            topics.add(baseName + kafkaProperties.getDltSuffix());
                            if (withRetry) {
                                topics.add(baseName + kafkaProperties.getRetrySuffix());
                            }
                            return topics.stream();
                        })
                .collect(Collectors.toList());
    }

    private List<String> getConsumerTopicNames() {

        List<String> topicsNames = new ArrayList<>();

        topicsNames.addAll(topicNamesByPrefix(kafkaProperties.getTopicPrefix(), false));

        return topicsNames;
    }

    private List<NewTopic> createTopicListByPrefix(
            String topicPrefix,
            int partitions,
            int dltPartitions,
            boolean withRetry,
            Collection<String> suffixes) {
        return suffixes.stream()
                .flatMap(
                        country -> {
                            String baseName = (topicPrefix + "-" + country).toLowerCase();
                            HashSet<NewTopic> newTopics = new HashSet<>();
                            newTopics.add(TopicBuilder.name(baseName).partitions(partitions).build());
                            newTopics.add(
                                    TopicBuilder.name(baseName + kafkaProperties.getDltSuffix())
                                            .partitions(dltPartitions)
                                            .build());
                            if (withRetry) {
                                newTopics.add(
                                        TopicBuilder.name(baseName + kafkaProperties.getRetrySuffix())
                                                .partitions(dltPartitions)
                                                .build());
                            }
                            return newTopics.stream();
                        })
                .collect(Collectors.toList());
    }

    private static BiFunction<ConsumerRecord<?, ?>, Exception, TopicPartition>
    dltDestinationResolverBuilder(String retrySuffix, String dltSuffix) {

        return (consumerRecord, exception) -> {
            String dltTopicName = consumerRecord.topic().replace(retrySuffix, "") + dltSuffix;
            return new TopicPartition(dltTopicName, -1);
        };
    }
}