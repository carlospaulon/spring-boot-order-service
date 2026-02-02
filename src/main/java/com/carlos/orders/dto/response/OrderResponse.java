package com.carlos.orders.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(
        Long id,
        String product,
        BigDecimal price,
        Integer quantity,
        BigDecimal total,
        LocalDateTime createdAt
) {
}
