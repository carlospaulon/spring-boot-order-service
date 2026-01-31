package com.carlos.orders.messaging.consumer;

import com.carlos.orders.model.Order;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderKafkaConsumer {
    @KafkaListener(topics = "order-created-topic", groupId = "order-group")
    public void consume(Order order) {
        System.out.println("Pedido recebido: " + order.getId());
    }
}
