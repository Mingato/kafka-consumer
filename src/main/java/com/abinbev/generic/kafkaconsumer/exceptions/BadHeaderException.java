package com.abinbev.generic.kafkaconsumer.exceptions;


import com.abinbev.generic.kafkaconsumer.constants.ConsumerConstants;

public class BadHeaderException extends GlobalException {

    private static final long serialVersionUID = -8720880588957774895L;

    private BadHeaderException(final Issue issue) {

        super(issue);
    }

    public static BadHeaderException headerIsNull() {

        return new BadHeaderException(
                new Issue(IssueEnum.HEADER_IS_NULL, ConsumerConstants.KAFKA_ORDERS_HEADER));
    }

    public static BadHeaderException timestampNotFound() {

        return new BadHeaderException(
                new Issue(IssueEnum.HEADER_NOT_FOUND, ConsumerConstants.HEADER_TIMESTAMP));
    }

    public static BadHeaderException countryNotFound() {

        return new BadHeaderException(
                new Issue(IssueEnum.HEADER_NOT_FOUND, ConsumerConstants.HEADER_COUNTRY));
    }

    public static BadHeaderException requestTraceIdNotFound() {

        return new BadHeaderException(
                new Issue(IssueEnum.HEADER_NOT_FOUND, ConsumerConstants.HEADER_REQUEST_TRACE_ID));
    }
}
