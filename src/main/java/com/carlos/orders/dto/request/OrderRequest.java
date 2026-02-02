package com.carlos.orders.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OrderRequest(
        //somente isto para request
        @NotBlank
        //string
        String product,

        @NotNull
        @Positive
        //int
        Integer quantity,

        @NotNull
        @Positive
        //bigdecimal
        BigDecimal price
) {
}
