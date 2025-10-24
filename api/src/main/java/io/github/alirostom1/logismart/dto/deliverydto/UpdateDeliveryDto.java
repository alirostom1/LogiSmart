package io.github.alirostom1.logismart.dto.deliverydto;

import io.github.alirostom1.logismart.util.CustomValidUUID;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDeliveryDto {

    private UUID id;

    @NotBlank(message = "Recipient name is required")
    @Size(min = 2, max = 100, message = "Recipient name must be between 2 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Recipient name can only contain letters and spaces")
    private String recipient;

    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be positive")
    @DecimalMin(value = "0.1", message = "Weight must be at least 0.1 kg")
    @DecimalMax(value = "100.0", message = "Weight cannot exceed 100 kg")
    private Double weight;

    @NotBlank(message = "Address is required")
    @Size(min = 5, max = 255, message = "Address must be between 5 and 255 characters")
    private String address;

    @NotNull(message = "Status is required")
    @Pattern(regexp = "^(PREPARATION|IN_TRANSIT|DELIVERED)$",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Status must be: PREPARATION, IN_TRANSIT, or DELIVERED")
    private String status;

    @NotBlank(message = "Courier ID is required")
    @CustomValidUUID
    private String courrierID;
}
