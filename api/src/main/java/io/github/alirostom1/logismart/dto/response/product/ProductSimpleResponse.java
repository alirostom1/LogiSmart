package io.github.alirostom1.logismart.dto.response.product;

public record ProductSimpleResponse(
        String id,
        String name,
        String category,
        double unitPrice
) {
}
