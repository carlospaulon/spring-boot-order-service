package com.carlos.orders.repository;

import com.carlos.orders.model.OrderLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderMongoRepository extends MongoRepository<OrderLog, String> {
}
