package io.github.alirostom1.logismart.dto.response.deliveryhistory;

public record DeliveryHistorySimpleResponse(
        String id,
        String status,
        String comment,
        String updatedAt
) {
}
