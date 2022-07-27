package com.abinbev.beesforce.kafkaconsumer.exceptions;


import org.springframework.http.HttpStatus;

public class RelayHttpException extends RuntimeException {

    private String entity;

    private String requestTraceId;

    private HttpStatus statusCode;

    private String errorMsg;

    public RelayHttpException(
            final String message,
            final String requestTraceId,
            final String entity,
            final HttpStatus statusCode,
            final String errorMsg) {
        super(
                String.format(
                        "%s # ERROR: %s # ENTITY: %s # REQUEST TRACE ID: %s",
                        message, errorMsg, entity, requestTraceId));
        this.requestTraceId = requestTraceId;
        this.errorMsg = errorMsg;
        this.statusCode = statusCode;
        this.entity = entity;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(final HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(final String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getRequestTraceId() {
        return requestTraceId;
    }

    public void setRequestTraceId(final String requestTraceId) {
        this.requestTraceId = requestTraceId;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }
}

