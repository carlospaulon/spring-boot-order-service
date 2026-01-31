package com.carlos.orders.messaging.producer;

import com.carlos.orders.model.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderProducer {
    private final RabbitTemplate rabbitTemplate;

    public OrderProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(Order order) {
        rabbitTemplate.convertAndSend(
                "order.exchange",
                "order.created",
                order
        );
    }
}