package com.abinbev.generic.kafkaconsumer.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Issue implements Serializable {
    private static final long serialVersionUID = 2405172041950251807L;

    @JsonProperty private String code;

    @JsonProperty private String message;

    @JsonProperty private final boolean retryable;

    public Issue(final IssueEnum issue, final Object... args) {

        code = issue.getCode();
        message = issue.getFormattedMessage(args);
        retryable = true;
    }

    public String getCode() {

        return code;
    }

    public void setCode(final String code) {

        this.code = code;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(final String message) {

        this.message = message;
    }

    @Override
    public String toString() {

        return String.format("Issue{code= %s, message='%s', retryable='%s'}", code, message, retryable);
    }
}
