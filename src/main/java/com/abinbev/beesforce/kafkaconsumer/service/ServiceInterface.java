package com.abinbev.beesforce.kafkaconsumer.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ServiceInterface {

    public void storeObject(final ConsumerRecord<String, Object> record);
}
