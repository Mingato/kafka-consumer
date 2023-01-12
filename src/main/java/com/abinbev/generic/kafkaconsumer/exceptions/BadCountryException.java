package com.abinbev.generic.kafkaconsumer.exceptions;

public class BadCountryException extends GlobalException {

    private static final long serialVersionUID = -8720880588957774895L;

    private BadCountryException(final Issue issue) {

        super(issue);
    }

    public static BadCountryException invalidCountry(final String country) {

        return new BadCountryException(new Issue(IssueEnum.COUNTRY_NOT_EXIST, country));
    }
}
