package org.example.temporal.model;

import lombok.Builder;

@Builder
public record CreateOrderRequest(
        String customerId,
        String productId,
        Integer quantity
) {
}
