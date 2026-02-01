package com.carlos.orders.controller;

import com.carlos.orders.dto.request.OrderRequest;
import com.carlos.orders.dto.response.OrderResponse;
import com.carlos.orders.model.Order;
import com.carlos.orders.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderRequest request) {
        Order order = orderService.create(toEntity(request));
        return ResponseEntity.ok(toResponse(order));
    }

    private OrderResponse toResponse(Order order) {
        BigDecimal total = order.getPrice()
                .multiply(BigDecimal.valueOf(order.getQuantity()))
                .setScale(2, RoundingMode.HALF_UP);

        return new OrderResponse(
                order.getId(),
                order.getProduct(),
                order.getPrice(),
                order.getQuantity(),
                total,
                order.getCreatedAt()
        );
    }

    private Order toEntity(OrderRequest request) {
        Order order = new Order();
        order.setProduct(request.product());
        order.setPrice(request.price());
        order.setQuantity(request.quantity());

        return order;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> find(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(orderService.findById(id)));
    }
}
