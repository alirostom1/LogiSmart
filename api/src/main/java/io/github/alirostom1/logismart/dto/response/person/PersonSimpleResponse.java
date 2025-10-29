package io.github.alirostom1.logismart.dto.response.person;

public record PersonSimpleResponse(
    String id,
    String lastName,
    String firstName,
    String email,
    String telephone,
    String address
) {
}
