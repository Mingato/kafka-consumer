package com.abinbev.generic.kafkaconsumer.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ConsumerConstants {

    public static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String HEADER_REQUEST_TRACE_ID = "requestTraceId";
    public static final String HEADER_COUNTRY = "country";
    public static final String HEADER_TIMESTAMP = "timestamp";
    public static final String KAFKA_ORDERS_HEADER = "kafka_orders_header";

    // Database fields
    public static final String ID_DATABASE_KEY = "_id";
    public static final String ACCOUNT_ID = "accountId";
    public static final String SORT_KEY = "sortKey";
    public static final String UPDATED_AT = "updatedAt";
    public static final String ORDER = "order";
    public static final String ORDER_NUMBER = "orderNumber";
}
