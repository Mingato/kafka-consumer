package com.abinbev.beesforce.kafkaconsumer.formatters;

import com.abinbev.beesforce.kafkaconsumer.constants.ConsumerConstants;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class OffsetDateTimeSerializer extends JsonSerializer<OffsetDateTime> {

    @Override
    public void serialize(
            final OffsetDateTime source, final JsonGenerator generator, final SerializerProvider provider)
            throws IOException {

        if (source != null) {
            final DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern(ConsumerConstants.DATE_PATTERN);
            generator.writeString(formatter.format(source));
        } else {
            generator.writeNull();
        }
    }
}

