package com.abinbev.beesforce.kafkaconsumer.constants;

public class ApiConstants {

  public static final String REQUEST_TRACE_ID_HEADER = "requestTraceId";
  public static final String COUNTRY_HEADER = "country";

  public static final String ENTITY_HEADER = "entity";
  public static final String VERSION_HEADER = "version";
  public static final String VENDOR_ID_HEADER = "vendorId";
  public static final String OPERATION_HEADER = "operation";

  public static final String TIMEZONE_HEADER = "timezone";
  public static final String SKU_ID_HEADER = "skuId";
  public static final String UNAUTHORIZED_ACCESS = "Unauthorized access";
  public static final String ACCESS_DENIED = "Access denied";
  public static final String API_TITLE = "AB-Inbev B2B Data Ingestion Relay Service";
  public static final String API_DESCRIPTION =
      "\"This is the Data Ingestion API. It allows you to integrate data with the bees Platform.\"";
  public static final String API_LICENSE = "Anheuser-Busch InBev © ";
  public static final String API_VERSION_1_LABEL = "API v1";
  public static final String TIMESTAMP_HEADER = "x-timestamp";
  public static final String HEADER_CONTENT_TYPE = "Content-Type";
  public static final String POST_METHOD = "POST";
  public static final String DELETE_METHOD = "DELETE";

  public static final String API_OPERATION_CREATE_PRICES_FOR_CUSTOMERS = "Group Pricing Creation";
  public static final String API_OPERATION_CREATE_PRICES_FOR_CUSTOMERS_NOTES =
      "Create prices for multiple customers.";
  public static final String HTTP_ACCEPT_MESSAGE = "Accept";
  public static final String HTTP_BAD_REQUEST_MESSAGE = "Bad Request";
  public static final String HTTP_FORBIDDEN_MESSAGE = "Forbidden";
  public static final String HTTP_METHOD_NOT_ALLOWED_MESSAGE = "Method Not Allowed";
  public static final String HTTP_METHOD_NOT_IMPLEMENTED_MESSAGE = "Not Implemented";
  public static final String HTTP_INTERNAL_SERVER_ERROR_MESSAGE = "Internal Server Error";
  public static final String HTTP_SERVICE_UNAVAILABLE_MESSAGE =
      "Service Unavailable. This error can be a result of queue not available. In this case, nothing will be processed.";
  public static final String REQUEST_TRACE_ID_DESCRIPTION =
      "Cross transaction unique ID. Used to track the request across systems.";
  public static final String AUTHORIZATION_DESCRIPTION =
      "JWT with the authorized role to access the relay.";
  public static final String TIMEZONE_DESCRIPTION =
      "Timezone that must be considered when applying date processing for that customer. In other words, this is the customer's timezone. The official web site responsible for maintaining the list of timezones is https://www.iana.org/time-zones. To identify the timezone code for your customer, download the latest version of the list (such as tzdb-2019a.tar.lz), extract the files, go to the extracted folder, run make, and execute the tzselect file. It is an utility that will help you find your timezone. The following link may also help you identifying the customer's timezone: https://en.wikipedia.org/wiki/List_of_tz_database_time_zones,";
  public static final String MALFORMED_REQUEST_EXCEPTION_MESSAGE = "Malformed Request";
  public static final String ARGUMENT_TYPE_MISMATCH = "%s: The value should be an %s.";
  public static final String REQUIRED_FIELD_EXCEPTION_MESSAGE =
      "The field %s is required and could not be empty.";
  public static final String METHOD_NOT_ALLOWED_EXCEPTION_MESSAGE =
      "%s method is not supported for this request. Supported methods are [%s]";
  public static final String UNEXPECTED_ERROR_EXCEPTION_MESSAGE =
      "Unexpected error. Please contact system administrator.";
  public static final String CONTENT_TYPE_NOT_SUPPORTED = "The header Content-Type is not valid";
  public static final String SERVICE_UNAVAILABLE_EXCEPTION_MESSAGE = "Service unavailable";
  public static final String API_OPERATION_DELETE_PRICES = "Price Deletion";
  public static final String API_OPERATION_DELETE_PRICES_NOTES =
      "Delete price information for specific customers by SKU.";
  public static final String ROUTING_KEY_PREFIX = ".*";
  public static final String SERVICE_NAME_LABEL = "ServiceName";
  public static final String DATA_INGESTION_RELAY_SERVICE = "data_ingestion-relay-service";
  public static final String ROLE_BACKEND = "BACKEND";
  public static final String REALM_VALUE = "RELAY";
  public static final String TOGGLE_CHECK_VAR_NAME = "check";
  public static final String TOGGLE_FILTER_VAR_NAME = "filter";
  public static final String TOGGLE_TTL_VAR_NAME = "ttl_minutes";
  public static final String TOGGLE_PREFIX = "data_ingestion";
  public static final String TOGGLE_SUFIX_CLEANSING = "cleansing";
  public static final String TOGGLE_SUFIX_KAFKA = "kafka";
  public static final String REWARDS_REQUEST_KEY_PATTERN = "accountId:%s-orderId:%s";

  // Repositories
  public static final String RESPONSE_CODE = "RESPONSE CODE: {}";
  public static final String RELAY_ASYNC_CLIENT_EXCEPTION =
      "Relay Async Client Exception, code {}, entity {}, operation {}, version {}.";
  public static final String RELAY_ASYNC_SERVER_EXCEPTION =
      "Relay Async Server Exception, code {}, entity {}, operation {}, version {}.";
  public static final String RELAY_SYNC_CLIENT_EXCEPTION =
      "Relay Sync Client Exception, code {}, entity {}, operation {}, version {}.";
  public static final String RELAY_SYNC_SERVER_EXCEPTION =
      "Relay Sync Server Exception, code {}, entity {}, operation {}, version {}.";
  public static final String OTHER_ASYNC_EXCEPTION =
      "Other Async Exception, entity {}, operation {}, version {}.";
  public static final String OTHER_SYNC_EXCEPTION =
      "Other Sync Exception, entity {}, operation {}, version {}.";
  public static final String METHOD_NOT_SUPPORTED = "Method not supported.";

  // Handlers
  public static final String FILTERED_PAYLOAD =
      "The entire payload has been filtered by cleansing, entity {}, traceId {}.";
  public static final String DUPLICATE_PAYLOAD =
      "Duplicate payload detected by cleansing, entity {}, traceId {}.";
  public static final String FAILED_TO_ADD_PAYLOAD_TO_FEEDBACK_LAYER =
      "Failed to add payload to feedback layer event.";
  public static final String GENERIC_HANDLER = "generic-handler";

  public static final String ISO_8601_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

  private ApiConstants() {}
}
