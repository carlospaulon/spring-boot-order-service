package com.carlos.orders.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Order Service",
                version = "1.0",
                description = "API para gerenciamento de pedidos (estudo com Kafka e RabbitMQ)"
        )
)
public class OpenApiConfig {
}
