package com.abinbev.generic.kafkaconsumer.repository;

import com.abinbev.generic.kafkaconsumer.listener.GenericKafkaListener;
import com.abinbev.ontaptestutils.mongo.SingleMongoDbInitializer;
import integration.AbstractContainerBaseTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(initializers = SingleMongoDbInitializer.class)
class OrderHistoryRepositoryIT extends AbstractContainerBaseTest {

    @Autowired
    MongoOperations mongoOperations;

    @MockBean
    GenericKafkaListener ordersListener;

    @SpyBean
    GenericRepository orderHistoryRepository;

    public static final String COLLECTION_NAME = "BR-OrdersHistory";

    @BeforeEach
    void setUp() {
        MDC.put("country", "BR");
        mongoOperations.dropCollection(COLLECTION_NAME);
    }

    @AfterEach
    void resetVerify(){
        MDC.clear();
    }

    @Test
    void shouldSaveOrderWrapperWhenReceivedOrderNotExists() {

        /*final Order order = OrderHelpers.getOrder();
        final OrderWrapper expectedOrderWrapper = GenericWrapperHelpers.getOrderWrapper(order);

        orderHistoryRepository.upsert(expectedOrderWrapper, null);

        Criteria criteria = where("order.orderNumber").is(expectedOrderWrapper.getOrder().getOrderNumber());
        OrderWrapper actualOrderWrapper = mongoOperations.find(new Query(criteria), OrderWrapper.class,
                COLLECTION_NAME).stream().findFirst().get();

        assertEquals(expectedOrderWrapper.getOrder().getOrderNumber(), actualOrderWrapper.getOrder().getOrderNumber());
        assertEquals(expectedOrderWrapper.getOrder().getAccountId(), actualOrderWrapper.getOrder().getAccountId());
        assertEquals(expectedOrderWrapper.getOrder().getChannel(), actualOrderWrapper.getOrder().getChannel());*/
    }

    @Test
    void shouldUpdateOrderWrapperWhenReceivedOrderExists() {

        /*final Order order = OrderHelpers.getOrderWithTotal(BigDecimal.TEN);
        final OrderWrapper expectedOrderWrapper = GenericWrapperHelpers.getOrderWrapper(order);
        BigDecimal expectedTotal = BigDecimal.valueOf(50L);

        OrderWrapper orderWrapperInsert = mongoOperations.insert(GenericWrapperHelpers
                .getOrderWrapper(OrderHelpers.getOrderWithTotal(expectedTotal)), COLLECTION_NAME);

        orderHistoryRepository.upsert(expectedOrderWrapper, orderWrapperInsert);

        Criteria criteria = where("order.orderNumber").is(orderWrapperInsert.getOrder().getOrderNumber());
        OrderWrapper actualOrderWrapper = mongoOperations.find(new Query(criteria), OrderWrapper.class,
                COLLECTION_NAME).stream().findFirst().get();

        assertEquals(expectedOrderWrapper.getOrder().getOrderNumber(), actualOrderWrapper.getOrder().getOrderNumber());
        assertEquals(expectedOrderWrapper.getOrder().getAccountId(), actualOrderWrapper.getOrder().getAccountId());
        assertEquals(expectedOrderWrapper.getOrder().getChannel(), actualOrderWrapper.getOrder().getChannel());
        assertEquals(expectedTotal, actualOrderWrapper.getOrder().getOriginalTotal());*/
    }
}
