package com.abinbev.generic.kafkaconsumer.exceptions;

public class UnauthorizedRequest extends RuntimeException {

    private final RelayHttpException relayHttpException;

    public UnauthorizedRequest(RelayHttpException relayHttpException) {
        this.relayHttpException = relayHttpException;
    }
}