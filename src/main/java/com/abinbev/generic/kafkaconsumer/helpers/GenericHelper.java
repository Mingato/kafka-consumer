package com.abinbev.generic.kafkaconsumer.helpers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

@Slf4j
@Service
public class GenericHelper {

    @NotNull
    public Map<Object, Object> getObjectObjectMap(Object object) {
        final ObjectMapper mapper = new ObjectMapper();
        Map<Object, Object> mapFromString = new HashMap<>();
        try {
            mapFromString = mapper.readValue(object.toString(), new TypeReference<>() {
            });
        } catch (IOException e) {
            log.error("Exception launched while trying to parse String to Map.", e);
        }

        return mapFromString;
    }

    public String generateTransparencySortKey(final String date, final Long createdAt) {
        long deliveryDate = 0L;
        if (date == null || StringUtils.isEmpty(date)) {
            deliveryDate = Long.MAX_VALUE;
        } else {
            deliveryDate =
                    LocalDate.parse(DateRegexHelper.getFirstDateFound(date))
                            .atStartOfDay()
                            .atZone(ZoneId.of("UTC"))
                            .toInstant()
                            .toEpochMilli();
        }
        return StringUtils.leftPad(format("%s%s", deliveryDate, createdAt), 60, '0');
    }
}
