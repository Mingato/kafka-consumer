package com.abinbev.generic.kafkaconsumer.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.IllegalFormatException;

public enum IssueEnum {
    GENERIC_ERROR("OTCM001", "Error Message: '%s' Stack Trace: '%s'"),
    HEADER_IS_NULL("OTCM002", "Header %s is null."),
    HEADER_NOT_FOUND("OTCM003", "Header %s not found."),
    COUNTRY_NOT_EXIST("OTCM014", "Country %s not exist.");

    private static final Logger LOGGER = LoggerFactory.getLogger(IssueEnum.class);

    private final String code;

    private final String message;

    IssueEnum(final String code, final String message) {

        this.code = code;
        this.message = message;
    }

    public String getCode() {

        return code;
    }

    public String getMessage() {

        return message;
    }

    public String getFormattedMessage(final Object... args) {

        if (message == null) {
            return "";
        }

        try {
            return String.format(message, args);
        } catch (final IllegalFormatException e) {
            LOGGER.warn(e.getMessage(), e);
            return message.replace("%s", "");
        }
    }
}
