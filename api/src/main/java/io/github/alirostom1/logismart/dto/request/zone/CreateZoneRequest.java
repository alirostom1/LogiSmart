package io.github.alirostom1.logismart.dto.request.zone;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateZoneRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Postal code is required")
    @Positive(message = "Postal code must be positive")
    private int postalCode;
}