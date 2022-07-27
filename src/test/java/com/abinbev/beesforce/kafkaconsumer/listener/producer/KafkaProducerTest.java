package com.abinbev.beesforce.kafkaconsumer.listener.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaProducerTest {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void send(final ProducerRecord<String, Object> record) {
        log.info("sending payload='{}' to topic='{}' with headers='{}'",
                record.value(), record.topic(), record.headers());

        kafkaTemplate.send(record);
    }
}
