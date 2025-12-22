package io.github.alirostom1.logismart.dto.request.zone;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateZoneRequest(
    @NotBlank String name,
    @Size(max = 10) String code
) {}
