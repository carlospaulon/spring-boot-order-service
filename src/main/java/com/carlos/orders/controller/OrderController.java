package com.carlos.orders.controller;

import com.carlos.orders.dto.request.OrderRequest;
import com.carlos.orders.dto.response.OrderResponse;
import com.carlos.orders.model.Order;
import com.carlos.orders.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Tag(name = "createOrder")
    @Operation(summary = "Criar novo pedido", description = "Cria um pedido e publica eventos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderRequest request) {
        Order order = orderService.create(toEntity(request));
        return ResponseEntity.ok(toResponse(order));
    }

    @Tag(name = "find")
    @Operation(summary = "Pega um pedido por id", description = "Pega um pedido e seus detalhes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the order",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Order.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content) })
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> find(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(orderService.findById(id)));
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
}
