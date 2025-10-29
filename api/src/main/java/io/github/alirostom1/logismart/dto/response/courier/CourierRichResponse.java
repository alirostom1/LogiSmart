package io.github.alirostom1.logismart.dto.response.courier;

import io.github.alirostom1.logismart.dto.response.delivery.DeliverySimpleResponse;
import io.github.alirostom1.logismart.dto.response.zone.ZoneSimpleResponse;

import java.util.List;

public record CourierRichResponse(
        String id,
        String firstName,
        String lastName,
        String phone,
        String vehicle,
        List<DeliverySimpleResponse> deliveries,
        ZoneSimpleResponse zone
) {
}
