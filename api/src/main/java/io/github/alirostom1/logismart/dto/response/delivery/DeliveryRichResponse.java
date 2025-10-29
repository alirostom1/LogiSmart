package io.github.alirostom1.logismart.dto.response.delivery;


import io.github.alirostom1.logismart.dto.response.courier.CourierSimpleResponse;
import io.github.alirostom1.logismart.dto.response.deliveryhistory.DeliveryHistorySimpleResponse;
import io.github.alirostom1.logismart.dto.response.deliveryproduct.DeliveryProductResponse;
import io.github.alirostom1.logismart.dto.response.person.PersonSimpleResponse;
import io.github.alirostom1.logismart.dto.response.zone.ZoneSimpleResponse;

import java.util.List;

public record DeliveryRichResponse(
        String id,
        String description,
        double weight,
        String status,
        String priority,
        String cityDestination,
        CourierSimpleResponse courier,
        PersonSimpleResponse sender,
        PersonSimpleResponse recipient,
        ZoneSimpleResponse zone,
        List<DeliveryProductResponse> products,
        List<DeliveryHistorySimpleResponse> history
) {
}
