package com.abinbev.beesforce.kafkaconsumer.formatters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class OffsetDateTimeDeserializer extends JsonDeserializer<OffsetDateTime> {

    @Override
    public OffsetDateTime deserialize(final JsonParser source, final DeserializationContext context)
            throws IOException {
        final LocalDate localDate = LocalDate.ofEpochDay(Long.parseLong(source.getText()));
        return OffsetDateTime.of(localDate, LocalTime.NOON, ZoneOffset.UTC);
    }
}
