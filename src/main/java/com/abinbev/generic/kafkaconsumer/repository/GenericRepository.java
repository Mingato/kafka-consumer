package com.abinbev.generic.kafkaconsumer.repository;


import com.abinbev.generic.kafkaconsumer.helpers.GenericHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class GenericRepository {

    private final MongoOperations mongoOperations;

    private final GenericHelper genericHelper;

    @Value("${spring.kafka.topicPrefix}")
    private String topicPrefix;

    @Value("${spring.data.mongodb.collectionName}")
    private String collectionName;

    public GenericRepository(final MongoOperations mongoOperations, GenericHelper genericHelper) {
        this.mongoOperations = mongoOperations;
        this.genericHelper = genericHelper;
    }

    public void insert(final Object object) {
        Map<Object, Object> mapFromString = genericHelper.getObjectObjectMap(object);

        mongoOperations.save(mapFromString, getCollectionNameByVendorId(((Map<Object, Object>)mapFromString.get("header")).get("vendorId").toString()));
    }


    public String getCollectionNameByVendorId(String vendorId) {

        return new StringBuilder()
                .append(collectionName)
                .append("-")
                .append(StringUtils.upperCase(vendorId))
                .toString();
    }
}
