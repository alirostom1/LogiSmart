package io.github.alirostom1.logismart.dto.request.zone;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import java.util.List;

public record CreateZoneWithPostalCodesRequest(
        @NotBlank String name,
        @Size(max = 10) String code,
        @NotEmpty List<@NotBlank String> postalCodes
) {}