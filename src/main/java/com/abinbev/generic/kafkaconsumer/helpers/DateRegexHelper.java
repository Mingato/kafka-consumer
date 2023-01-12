package com.abinbev.generic.kafkaconsumer.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateRegexHelper {

    private static final String REGEX_DATE = "([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))";

    private DateRegexHelper() {}

    public static String getFirstDateFound(final String str) {
        final List<String> tagValues = new ArrayList<String>();

        final Pattern TAG_REGEX = Pattern.compile(REGEX_DATE, Pattern.DOTALL);
        final Matcher matcher = TAG_REGEX.matcher(str);
        while (matcher.find()) {
            tagValues.add(matcher.group(1));
        }
        return tagValues.get(0);
    }
}
