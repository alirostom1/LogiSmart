package io.github.alirostom1.logismart.dto.response.delivery;

public record DeliverySimpleResponse(
        String id,
        String description,
        double weight,
        String status,
        String priority,
        String cityDestination
){
}
