package com.abinbev.generic.kafkaconsumer.service;

import com.abinbev.generic.kafkaconsumer.repository.GenericRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class OrderHistoryServiceTest {

    @Mock
    GenericRepository orderHistoryRepository;

    @InjectMocks
    MongoService orderHistoryService;

    @Test
    void shouldSaveNewOrderWhenReceiveOrderNotExists() {

        /*final Order order = OrderHelpers.getOrder();
        final OrderWrapper ExpectedOrderWrapper = GenericWrapperHelpers.getOrderWrapperWithoutDate(order);

        when(orderHistoryRepository.findByOrderNumber(anyString())).thenReturn(null);
        orderHistoryService.upsert(order);

        verify(orderHistoryRepository).findByOrderNumber(order.getOrderNumber());
        verify(orderHistoryRepository).upsert(ExpectedOrderWrapper, null);*/
    }

    @Test
    void shouldUpdateOrderWhenReceiveOrderExists() {

        /*final Order order = OrderHelpers.getOrder();
        final OrderWrapper ExpectedOrderWrapper = GenericWrapperHelpers.getOrderWrapperWithoutDate(order);
        final OrderWrapper ExpectedOrderWrapperReturn = GenericWrapperHelpers.getOrderWrapper();

        when(orderHistoryRepository.findByOrderNumber(anyString())).thenReturn(ExpectedOrderWrapperReturn);
        orderHistoryService.upsert(order);

        verify(orderHistoryRepository).findByOrderNumber(order.getOrderNumber());
        verify(orderHistoryRepository).upsert(ExpectedOrderWrapper, ExpectedOrderWrapperReturn);*/
    }
}
