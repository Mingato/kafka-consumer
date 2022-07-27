package com.abinbev.beesforce.kafkaconsumer.service;

import com.abinbev.beesforce.kafkaconsumer.repository.GenericRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractMongoService implements ServiceInterface{

    private final GenericRepository genericRepository;

    @Autowired
    public AbstractMongoService(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }

    @Override
    public void storeObject(ConsumerRecord<String, Object> record) {
        log.info(record.toString());
        genericRepository.insert(record.value());
    }
}
