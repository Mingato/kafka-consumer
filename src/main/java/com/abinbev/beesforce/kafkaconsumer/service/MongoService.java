package com.abinbev.beesforce.kafkaconsumer.service;

import com.abinbev.beesforce.kafkaconsumer.repository.GenericRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MongoService extends AbstractMongoService {

    public MongoService(GenericRepository genericRepository) {
        super(genericRepository);
    }
}
