package com.abinbev.beesforce.kafkaconsumer.exceptions;

public class GlobalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final Issue issue;

    protected GlobalException(final Issue issue) {

        super(issue.getMessage());
        this.issue = issue;
    }

    public Issue getIssue() {

        return issue;
    }
}

