package io.github.alirostom1.logismart.dto.response.person;

import io.github.alirostom1.logismart.dto.response.delivery.DeliverySimpleResponse;

import java.util.List;

public record RecipientRichResponse(
        PersonSimpleResponse details,
        List<DeliverySimpleResponse> deliveriesReceived
){
}
