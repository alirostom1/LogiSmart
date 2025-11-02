package io.github.alirostom1.logismart.dto.request.delivery;

import io.github.alirostom1.logismart.model.enums.DeliveryStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDeliveryStatusRequest {
    @NotNull(message = "Status is required")
    @Pattern(regexp = "^(|CREATED|COLLECTED|IN_STOCK|IN_TRANSIT|DELIVERED)$",
            message = "Status must be one of: CREATED, COLLECTED, IN_STOCK, IN_TRANSIT, DELIVERED")
    private String status;

    private String comment;
}