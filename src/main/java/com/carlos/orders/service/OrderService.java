package com.carlos.orders.service;

import com.carlos.orders.messaging.producer.OrderKafkaProducer;
import com.carlos.orders.messaging.producer.OrderProducer;
import com.carlos.orders.model.Order;
import com.carlos.orders.model.OrderLog;
import com.carlos.orders.repository.OrderMongoRepository;
import com.carlos.orders.repository.OrderRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMongoRepository mongoRepository;
    private final OrderKafkaProducer kafkaProducer;
    private final OrderProducer rabbitProducer;

    public OrderService(
            OrderRepository orderRepository,
            OrderMongoRepository mongoRepository,
            OrderKafkaProducer kafkaProducer,
            OrderProducer rabbitProducer
    ) {
        this.orderRepository = orderRepository;
        this.mongoRepository = mongoRepository;
        this.kafkaProducer = kafkaProducer;
        this.rabbitProducer = rabbitProducer;
    }

    @Cacheable("orders")
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
    }

    @Transactional
    public Order create(Order order) {
        Order saved = orderRepository.save(order);

        mongoRepository.save(
                new OrderLog(null, saved.getId(), LocalDateTime.now())
        );

        kafkaProducer.send(saved);
        rabbitProducer.publish(saved);

        return saved;
    }
}
