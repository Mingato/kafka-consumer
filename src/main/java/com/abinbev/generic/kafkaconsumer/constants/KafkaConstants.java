package com.abinbev.generic.kafkaconsumer.constants;

public class KafkaConstants {
    public static final String SECURITY_PROTOCOL = "SASL_PLAINTEXT";//SASL_SSL";
    public static final String SASL_MECHANISM = "PLAIN";
    public static final String BASIC_AUTH_CREDENTIALS_SOURCE = "USER_INFO";
    public static final String KAFKA_BASIC_AUTH_USER_INFO_FIELD = "basic.auth.user.info";

    private KafkaConstants() {}
}