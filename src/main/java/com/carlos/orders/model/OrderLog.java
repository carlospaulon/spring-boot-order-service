package com.carlos.orders.model;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "order_logs")
public class OrderLog {
    @Id
    private String id;
    private long orderId;
    private LocalDateTime createdAt;

    public OrderLog(String id, long orderId, LocalDateTime createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.createdAt = createdAt;
    }
}
