package com.abinbev.b2b.data.ingestion.feedback.layer.util;

import com.abinbev.b2b.IngestionTrackingRecord;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class TestDataFactory {

  public static final IngestionTrackingRecord INGESTION_TRACKING_RECORD =
      IngestionTrackingRecord.newBuilder()
          .setId(UUID.randomUUID().toString())
          .setTraceId("1234")
          .setParentTraceId("1234PARENT")
          .setOperation("EXTRACT")
          .setSteps(List.of("DESERIALIZE"))
          .setSuccess(false)
          .setErrorCode("Error Code")
          .setErrorMessage("Malformed Request.")
          .setCountry("BR")
          .setVendorId("WS-123")
          .setPayload("{\"test\":\"payload\"}")
          .setSourceSystem("ETL-CONSUMER")
          .setCreatedAt(Instant.now().toEpochMilli())
          .setDurationMs(0L)
          .build();
}
