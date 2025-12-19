package io.github.alirostom1.logismart.dto.request.delivery;

import io.github.alirostom1.logismart.util.ValidUUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignDeliveryRequest {
    @NotNull(message = "Courier ID is required")
    private Long courierId;
}