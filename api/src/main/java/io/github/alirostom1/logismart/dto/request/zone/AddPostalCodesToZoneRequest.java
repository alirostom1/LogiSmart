package io.github.alirostom1.logismart.dto.request.zone;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record AddPostalCodesToZoneRequest(
        @NotNull Long zoneId,
        @NotEmpty Set<@NotBlank String> postalCodes
) {}
