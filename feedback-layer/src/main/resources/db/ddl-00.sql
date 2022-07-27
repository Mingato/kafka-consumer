CREATE TABLE records (
  "timestamp"       TIMESTAMPTZ       NOT NULL,
  "id"              TEXT              PRIMARY KEY,
  "trace_id"        TEXT              NOT NULL,
  "parent_trace_id" TEXT              NOT NULL,
  "operation"       TEXT              NOT NULL,
  "steps"           TEXT ARRAY        NOT NULL,
  "success" 	    BOOLEAN           NOT NULL,
  "error_code"      TEXT              NULL,
  "error_message"   TEXT              NULL,
  "country"         TEXT              NOT NULL,
  "vendor_id"       TEXT              NOT NULL,
  "payload"         TEXT              NULL,
  "source_system"   TEXT              NOT NULL,
  "duration_ms"     BIGINT            NOT NULL,
  "created_at"      BIGINT            NOT NULL
);

CREATE TABLE status (
  "trace_id"               TEXT              PRIMARY KEY,
  "parent_trace_id"        TEXT              NOT NULL,
  "trace_id_failed_chunks" TEXT ARRAY        NOT NULL DEFAULT '{}',
  "status"                 TEXT              NOT NULL,
  "updated_at"             TIMESTAMPTZ       NOT NULL
);