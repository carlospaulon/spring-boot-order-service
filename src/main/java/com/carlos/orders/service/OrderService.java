package com.carlos.orders.service;

import com.carlos.orders.messaging.producer.OrderKafkaProducer;
import com.carlos.orders.messaging.producer.OrderProducer;
import com.carlos.orders.model.Order;
import com.carlos.orders.model.OrderLog;
import com.carlos.orders.repository.OrderMongoRepository;
import com.carlos.orders.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.messaging.MessagingException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
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
    @Retryable(
            value = {MessagingException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000L)
    )
    public Order create(Order order) {
        Order saved = orderRepository.save(order);
        log.info("Order created successfully - orderId: {}", saved.getId());


        mongoRepository.save(
                new OrderLog(null, saved.getId(), LocalDateTime.now())
        );
        try {
            kafkaProducer.send(saved);
            log.debug("Kafka message sent - orderId: {}", saved.getId());

            rabbitProducer.publish(saved);
            log.debug("Rabbit message sent - orderId: {}", saved.getId());


        } catch (Exception e) {
            log.error("Failed to create order - product: {}", order.getProduct());

            throw e;
        }


        return saved;
    }

    @Recover
    public Order recover(MessagingException e, Order order) {

        return order;
    }
}
