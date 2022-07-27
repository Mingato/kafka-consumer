package com.abinbev.beesforce.kafkaconsumer.validations;

import com.abinbev.beesforce.kafkaconsumer.constants.ConsumerConstants;
import com.abinbev.beesforce.kafkaconsumer.exceptions.BadHeaderException;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

@Component
public class HeadersGenericValidation {

    public void valid(MessageHeaders headers) {

        if (headers == null) {
            throw BadHeaderException.headerIsNull();
        }
        if (!headers.containsKey(ConsumerConstants.HEADER_TIMESTAMP)) {
            throw BadHeaderException.timestampNotFound();
        }
        if (!headers.containsKey(ConsumerConstants.HEADER_REQUEST_TRACE_ID)) {
            throw BadHeaderException.countryNotFound();
        }
        if (!headers.containsKey(ConsumerConstants.HEADER_COUNTRY)) {
            throw BadHeaderException.requestTraceIdNotFound();
        }
    }
}
