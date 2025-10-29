package io.github.alirostom1.logismart.dto.response.product;

import io.github.alirostom1.logismart.dto.response.delivery.DeliverySimpleResponse;

import java.util.List;

public record ProductRichResponse(
        String id,
        String name,
        String category,
        double unitPrice,
        List<DeliverySimpleResponse> deliveries
) {
}
