package io.github.alirostom1.logismart.dto.response.courier;

public record CourierSimpleResponse(
        String id,
        String firstName,
        String lastName,
        String phone,
        String vehicle
) {
}
