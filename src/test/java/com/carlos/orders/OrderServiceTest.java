package com.carlos.orders;

import com.carlos.orders.model.Order;
import com.carlos.orders.repository.OrderMongoRepository;
import com.carlos.orders.repository.OrderRepository;
import com.carlos.orders.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    OrderRepository repository;

    @Mock
    OrderMongoRepository mongoRepository;

    @InjectMocks
    OrderService service;

    @Test
    void shouldCreateOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setProduct("Livro");

        when(repository.save(any())).thenReturn(order);

        Order result = service.create(order);

        assertNotNull(result);
        verify(mongoRepository).save(any());
    }
}

