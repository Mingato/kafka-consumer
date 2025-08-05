# Kafka Consumer

A Spring Boot application that provides a generic Kafka consumer framework for processing messages with MongoDB storage, feedback layer integration, and comprehensive error handling.

## Overview

This project is a generic Kafka consumer service designed to:
- Consume messages from Kafka topics using Avro serialization
- Store processed data in MongoDB
- Provide feedback layer integration for message processing status
- Handle retry mechanisms and dead letter topics
- Support multiple countries and vendor configurations

## Features

- **Generic Kafka Consumer**: Configurable consumer for various message types
- **MongoDB Integration**: Automatic data persistence with Mongock for schema migrations
- **Feedback Layer**: Real-time processing status feedback
- **Error Handling**: Comprehensive error handling with retry mechanisms
- **Avro Support**: Built-in support for Apache Avro serialization
- **Docker Support**: Containerized deployment with Docker
- **New Relic Monitoring**: Application performance monitoring integration
- **Multi-Environment Support**: Configurable for different environments

## Technology Stack

- **Java 11**
- **Spring Boot 2.x**
- **Apache Kafka**
- **MongoDB**
- **Apache Avro**
- **Docker**
- **New Relic**
- **Maven**

## Prerequisites

- Java 11 or higher
- Maven 3.6+
- Docker (for containerized deployment)
- MongoDB instance
- Kafka cluster with Schema Registry
- New Relic account (for monitoring)

## Configuration

### Environment Variables

The application can be configured using the following environment variables:

#### Kafka Configuration
- `KAFKA_BOOTSTRAP_SERVER_CONFIG`: Kafka bootstrap servers
- `KAFKA_SCHEMA_REGISTRY`: Schema Registry URL
- `KAFKA_BROKER_USER`: Kafka broker username
- `KAFKA_BROKER_PASSWORD`: Kafka broker password
- `KAFKA_GENERIC_CONSUMER_GROUP`: Consumer group ID
- `KAFKA_TOPIC_PREFIX0`: Topic prefix for processing
- `KAFKA_TOPIC_PARTITIONS`: Number of topic partitions

#### MongoDB Configuration
- `MONGODB_CONNECTION_STRING`: MongoDB connection string
- `COLLECTION_NAME0`: Collection name for data storage

#### Service Configuration
- `SERVICE_NAME0`: Target service name
- `SERVICE_URL0`: Target service URL
- `FEIGN_CON_TIMEOUT`: Feign client connection timeout
- `FEIGN_READ_TIMEOUT`: Feign client read timeout

#### Feature Toggles
- `TOGGLE_COUNTRY`: Comma-separated list of enabled countries
- `TOGGLE_VENDOR_ID`: Vendor ID for filtering

#### Feedback Layer
- `FEEDBACK_LAYER_HEAP_SIZE`: Feedback layer cache heap size
- `FEEDBACK_LAYER_TTL_SEC`: Feedback layer cache TTL

### Application Properties

The main configuration is in `src/main/resources/application.yml`:

```yaml
spring:
  kafka:
    bootstrap-servers: ${KAFKA_BOOTSTRAP_SERVER_CONFIG}
    consumer:
      group-id: ${KAFKA_GENERIC_CONSUMER_GROUP}
      auto-offset-reset: earliest
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: io.confluent.kafka.serializers.KafkaAvroSerializer
```

## Building the Application

### Using Maven

```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package the application
mvn package

# Run the application
mvn spring-boot:run
```

### Using Docker

```bash
# Build the Docker image
docker build -t kafka-consumer .

# Run the container
docker run -p 8080:8080 kafka-consumer
```

## Usage

### Basic Setup

1. **Enable the Kafka Consumer** in your Spring Boot application:

```java
@EnableKafkaConsumer
@SpringBootApplication
public class YourApplication {
    public static void main(String[] args) {
        SpringApplication.run(YourApplication.class, args);
    }
}
```

2. **Configure your Kafka topics** in the application properties

3. **Implement your service interface** to handle message processing:

```java
@Service
public class YourService implements ServiceInterface {
    @Override
    public void storeObject(ConsumerRecord<String, Object> record) {
        // Your message processing logic here
    }
}
```

### Message Processing

The application automatically:
- Consumes messages from configured Kafka topics
- Validates message headers
- Processes messages through your service implementation
- Stores results in MongoDB
- Sends feedback to the feedback layer
- Handles retries and dead letter topics

### Error Handling

The application includes comprehensive error handling:
- Automatic retry mechanisms
- Dead letter topic support
- Error message translation
- Feedback layer integration for error reporting

## Project Structure

```
src/main/java/com/abinbev/generic/kafkaconsumer/
├── config/           # Configuration classes
├── constants/        # Application constants
├── exceptions/       # Custom exceptions
├── formatters/       # Data formatters
├── helpers/          # Utility helper classes
├── listener/         # Kafka message listeners
├── properties/       # Configuration properties
├── repository/       # Data access layer
├── service/          # Business logic services
├── validations/      # Validation logic
├── EnableKafkaConsumer.java
├── EnableKafkaConsumerRepository.java
└── KafkaConsumerApplication.java
```

## Monitoring

### New Relic Integration

The application includes New Relic monitoring:
- Performance metrics
- Error tracking
- Custom metrics for message processing
- Environment-specific configuration

### Logging

The application uses structured logging with:
- MDC (Mapped Diagnostic Context) for request tracing
- Configurable log levels
- Performance logging for message processing

## Development

### Running Tests

```bash
# Run all tests
mvn test

# Run with test containers
mvn test -Dspring.profiles.active=test
```

### Local Development

1. Set up local Kafka and MongoDB instances
2. Configure environment variables for local development
3. Run the application with `mvn spring-boot:run`

## Deployment

### Docker Deployment

The application includes a Dockerfile for containerized deployment:

```dockerfile
FROM adoptopenjdk/openjdk11:alpine-slim
WORKDIR /opt/app
COPY target/*.jar app.jar
COPY newrelic.yml .
ENTRYPOINT java ${JAVA_OPTS} -javaagent:newrelic-agent.jar -jar app.jar
```

### Kubernetes Deployment

The application can be deployed to Kubernetes with appropriate:
- ConfigMaps for configuration
- Secrets for sensitive data
- Service and Ingress configurations
- Resource limits and requests

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## License

This project is proprietary to AB InBev.

## Support

For support and questions, please contact the development team or create an issue in the project repository. 