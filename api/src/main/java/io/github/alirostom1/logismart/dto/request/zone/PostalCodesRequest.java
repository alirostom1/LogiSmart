package io.github.alirostom1.logismart.dto.request.zone;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record PostalCodesRequest(
        @NotEmpty Set<@NotBlank String> postalCodes
) {}

