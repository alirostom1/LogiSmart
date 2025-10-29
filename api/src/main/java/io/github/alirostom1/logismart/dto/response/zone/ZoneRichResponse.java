package io.github.alirostom1.logismart.dto.response.zone;

import io.github.alirostom1.logismart.dto.response.courier.CourierSimpleResponse;
import io.github.alirostom1.logismart.dto.response.delivery.DeliverySimpleResponse;

import java.util.List;

public record ZoneRichResponse(
        String id,
        String name,
        int postalCode,
        List<CourierSimpleResponse> couriers,
        List<DeliverySimpleResponse> deliveries
){
}
