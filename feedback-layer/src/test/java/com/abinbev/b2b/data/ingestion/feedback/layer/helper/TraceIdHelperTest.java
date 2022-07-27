package com.abinbev.b2b.data.ingestion.feedback.layer.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TraceIdHelperTest {

  @Test
  public void getTraceIdFromChunkTest() {
    String chunkedTraceId = "LT-034_505d5c58-d9d6-11eb-b8bc-0242ac130003-part-1";
    String traceId = "LT-034_505d5c58-d9d6-11eb-b8bc-0242ac130003";
    assertEquals(traceId, TraceIdHelper.getTraceIdFromChunk(chunkedTraceId));
  }

  @Test
  public void getTraceIdFromTraceIdTest() {
    String traceId = "LT-034_505d5c58-d9d6-11eb-b8bc-0242ac130003";
    assertEquals(traceId, TraceIdHelper.getTraceIdFromChunk(traceId));
  }
}
