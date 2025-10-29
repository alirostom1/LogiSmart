package io.github.alirostom1.logismart.dto.response.deliveryhistory;

import io.github.alirostom1.logismart.dto.response.delivery.DeliverySimpleResponse;

public record DeliveryHistoryRichResponse(
        String id,
        String status,
        String comment,
        String updatedAt,
        DeliverySimpleResponse delivery
) {
}
