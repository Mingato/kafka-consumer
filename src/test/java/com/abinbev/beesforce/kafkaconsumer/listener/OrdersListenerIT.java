package com.abinbev.beesforce.kafkaconsumer.listener;

import com.abinbev.beesforce.kafkaconsumer.KafkaConsumerApplication;
import com.abinbev.beesforce.kafkaconsumer.listener.producer.KafkaProducerTest;
import com.abinbev.beesforce.kafkaconsumer.repository.GenericRepository;
import com.abinbev.beesforce.kafkaconsumer.service.MongoService;
import com.abinbev.beesforce.kafkaconsumer.validations.HeadersGenericValidation;
import com.abinbev.ontaptestutils.kafka.SingleKafkaInitializer;
import com.abinbev.ontaptestutils.mongo.SingleMongoDbInitializer;
import integration.AbstractContainerBaseTest;
import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import utils.TestConstants;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static utils.TestConstants.*;

@ActiveProfiles("test")
@Import({KafkaProducerTest.class})
@SpringBootTest(classes = {KafkaConsumerApplication.class})
@DirtiesContext
@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = {SingleKafkaInitializer.class, SingleMongoDbInitializer.class})
class OrdersListenerIT extends AbstractContainerBaseTest {

    @SpyBean
    MongoOperations mongoOperations;

    @SpyBean
    GenericKafkaListener consumer;

    @Autowired
    KafkaProducerTest producer;

    @SpyBean
    MongoService orderHistoryService;

    @SpyBean
    HeadersGenericValidation headersOrdersValidation;

    @SpyBean
    GenericRepository orderHistoryRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Value("${spring.kafka.topic-prefix}")
    String topic;

    @Value("${spring.kafka.config.clientId}")
    String clientId;

    @Value("${spring.kafka.config.retries}")
    Integer retries;

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection(COLLECTION_NAME);
    }

    @Test
    @SneakyThrows
    void shouldInsertValidOrder() {

        /*final OrderMessage expectedOrderMessage = GenericMessageHelpers.createMessageDefault();
        final ProducerRecord<String, Object> expectedRecord = new ProducerRecord<>(topic, clientId, expectedOrderMessage);
        final Order expectedOrder = ConsumerHelper.convertAvroToOrderObject(expectedRecord.value().toString());
        final OrderWrapper expectedOrderWrapper = GenericWrapperHelpers.getOrderWrapperWithOrder(expectedOrder);

        final String expectedTimesTime = String.valueOf(Instant.now().toEpochMilli());
        setHeaders(expectedRecord, COUNTRY_BR, REQUEST_TRACE_ID_VALUE, expectedTimesTime);

        producer.send(expectedRecord);
        final CountDownLatch latch = new CountDownLatch(1);

        latch.await(DEFAULT_DELAY, TimeUnit.MILLISECONDS);

        final List<OrderWrapper> actualOrderWrapperList = mongoTemplate.findAll(OrderWrapper.class, COLLECTION_NAME);

        verify(headersOrdersValidation, only()).valid(any(MessageHeaders.class));
        assertNull(MDC.get(HEADER_REQUEST_TRACE_ID));
        assertNull(MDC.get(HEADER_COUNTRY));
        assertNull(MDC.get(HEADER_TIMESTAMP));
        verify(orderHistoryService, only()).upsert(any(Order.class));
        assertFalse(actualOrderWrapperList.isEmpty());
        assertEquals(actualOrderWrapperList.get(0).getAccountId(), expectedOrderWrapper.getAccountId());*/
    }

    @Test
    @SneakyThrows
    void shouldUpdateValidOrder() {
        Map expectedMessage = new HashMap<>();


        expectedMessage.put("entity", "Force");
        expectedMessage.put("version", "v1");
        expectedMessage.put("payload", "{\"name\":'\"name\"'}");

        final ProducerRecord<String, Object> expectedRecord = new ProducerRecord<>(topic, clientId, expectedMessage);
        final String expectedTimesTime = String.valueOf(Instant.now().toEpochMilli());
        setHeaders(expectedRecord, COUNTRY_CA, REQUEST_TRACE_ID_VALUE, expectedTimesTime);

        producer.send(expectedRecord);

        /*final OrderMessage expectedOrderMessage = GenericMessageHelpers.createMessageDefault();
        final ProducerRecord<String, Object> expectedRecord = new ProducerRecord<>(topic, clientId, expectedOrderMessage);

        final Order expectedOrder = ConsumerHelper.convertAvroToOrderObject(expectedRecord.value().toString());
        expectedOrder.setChannel("Mobile");
        final OrderWrapper expectedOrderWrapper = GenericWrapperHelpers.getOrderWrapperWithOrder(expectedOrder);
        final Query expectedQuery = query(Criteria.where(FIELD_ORDER_NUMBER).is(expectedOrderWrapper.getOrder().getOrderNumber()));

        final String expectedTimesTime = String.valueOf(Instant.now().toEpochMilli());
        setHeaders(expectedRecord, COUNTRY_BR, REQUEST_TRACE_ID_VALUE, expectedTimesTime);

        mongoTemplate.insert(expectedOrderWrapper, COLLECTION_NAME);

        producer.send(expectedRecord);
        final CountDownLatch latch = new CountDownLatch(1);
        latch.await(DEFAULT_DELAY, TimeUnit.MILLISECONDS);

        final OrderWrapper actualOrderWrapper = mongoTemplate.findAll(OrderWrapper.class, COLLECTION_NAME)
                .stream().findFirst().get();

        verify(headersOrdersValidation, only()).valid(any(MessageHeaders.class));
        assertNull(MDC.get(HEADER_REQUEST_TRACE_ID));
        assertNull(MDC.get(HEADER_COUNTRY));
        assertNull(MDC.get(HEADER_TIMESTAMP));
        assertEquals(actualOrderWrapper.getOrder().getChannel(), expectedOrder.getChannel());
        verify(orderHistoryService, only()).upsert(any(Order.class));*/
    }

    @Test
    @SneakyThrows
    void shouldNotProcessOrderFromInvalidCountries() {

        /*final OrderMessage expectedOrderMessage = GenericMessageHelpers.createMessageDefault();
        final ProducerRecord<String, Object> expectedRecord = new ProducerRecord<>(topic, clientId, expectedOrderMessage);
        final String expectedTimesTime = String.valueOf(Instant.now().toEpochMilli());
        setHeaders(expectedRecord, COUNTRY_CA, REQUEST_TRACE_ID_VALUE, expectedTimesTime);

        producer.send(expectedRecord);
        final CountDownLatch latch = new CountDownLatch(1);

        latch.await(DEFAULT_DELAY, TimeUnit.MILLISECONDS);

        assertSceneryWithoutRetry();*/
    }


    @Test
    @SneakyThrows
    void shouldRetryOnOrderMissing_RequestTraceId() {

        /*final OrderMessage expectedOrderMessage = GenericMessageHelpers.createMessageDefault();
        final ProducerRecord<String, Object> expectedRecord = new ProducerRecord<>(topic, clientId, expectedOrderMessage);

        expectedRecord.headers().add(COUNTRY, COUNTRY_BR.getBytes());
        expectedRecord.headers().add(TIMESTAMP, String.valueOf(Instant.now().toEpochMilli()).getBytes());

        producer.send(expectedRecord);
        final CountDownLatch latch = new CountDownLatch(1);

        latch.await(DEFAULT_DELAY, TimeUnit.MILLISECONDS);

        assertWithRetries();*/
    }


    @Test
    @SneakyThrows
    public void shouldSendDLTInvalidMassage() {

        /*final ProducerRecord<String, Object> orderMessageProducerRecord =
                new ProducerRecord<>(topic, clientId, "");
        setHeaders(orderMessageProducerRecord, COUNTRY_BR, REQUEST_TRACE_ID_VALUE, String.valueOf(Instant.now().toEpochMilli()));

        producer.send(orderMessageProducerRecord);
        final CountDownLatch latch = new CountDownLatch(1);

        latch.await(DEFAULT_DELAY, TimeUnit.MILLISECONDS);

        verify(consumer, only()).receive(any(), any());
        verify(headersOrdersValidation, only()).valid(any(MessageHeaders.class));
        assertNull(MDC.get(HEADER_REQUEST_TRACE_ID));
        assertNull(MDC.get(HEADER_COUNTRY));
        assertNull(MDC.get(HEADER_TIMESTAMP));*/
    }

    private void setHeaders(final ProducerRecord<String, Object> messageProducerRecord, final String country,
                            final String requestTraceId, final String timestamp) {

        messageProducerRecord.headers().add(COUNTRY, country.getBytes());
        messageProducerRecord.headers().add(TestConstants.REQUEST_TRACE_ID, requestTraceId.getBytes());
        messageProducerRecord.headers().add(TIMESTAMP, timestamp.getBytes());
    }

    private void assertSceneryWithoutRetry() {

        /*verify(consumer, never()).receive(any(), any());
        verify(headersOrdersValidation, never()).valid(any(MessageHeaders.class));
        assertNull(MDC.get(HEADER_REQUEST_TRACE_ID));
        assertNull(MDC.get(HEADER_COUNTRY));
        assertNull(MDC.get(HEADER_TIMESTAMP));
        verify(orderHistoryService, never()).upsert(any(Order.class));
        verify(orderHistoryRepository, never()).findByOrderNumber(anyString());
        verify(mongoOperations, never()).insert(any(OrderWrapper.class), eq(COLLECTION_NAME));*/
    }

    private void assertWithRetries() {

        /*final int wantedNumberOfInvocations = retries + 1;
        verify(consumer, times(wantedNumberOfInvocations)).receive(any(), any());
        verify(headersOrdersValidation, times(wantedNumberOfInvocations)).valid(any(MessageHeaders.class));
        assertNull(MDC.get(HEADER_REQUEST_TRACE_ID));
        assertNull(MDC.get(HEADER_COUNTRY));
        assertNull(MDC.get(HEADER_TIMESTAMP));
        verify(orderHistoryService, never()).upsert(any(Order.class));
        verify(orderHistoryRepository, never()).findByOrderNumber(anyString());
        verify(mongoOperations, never()).insert(any(OrderWrapper.class), eq(COLLECTION_NAME));*/
    }
}
