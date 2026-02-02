package com.carlos.orders.messaging.consumer;

import com.carlos.orders.config.RabbitConfig;
import com.carlos.orders.model.Order;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderRabbitConsumer {

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void consume(Order order) {
        System.out.println("Rabbit received order: " + order.getId());
    }
}

