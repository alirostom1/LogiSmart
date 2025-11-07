package io.github.alirostom1.logismart.dto.request.delivery;

import io.github.alirostom1.logismart.model.enums.DeliveryPriority;
import io.github.alirostom1.logismart.util.ValidUUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDeliveryRequest {
    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Destination city is required")
    private String destinationCity;

    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be positive")
    private Double weight;

    @Pattern(regexp = "^(|LOW|MEDIUM|HIGH)$",
            message = "Priority must be one of: LOW, MEDIUM, HIGH")
    private String priority;

    @NotBlank(message = "Sender ID is required")
    @ValidUUID
    private String senderId;

    @NotBlank(message = "Recipient ID is required")
    @ValidUUID
    private String recipientId;

    @NotBlank(message = "Zone ID is required")
    @ValidUUID
    private String zoneId;

    private List<DeliveryProductRequest> products;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeliveryProductRequest {
        @NotBlank(message = "Product ID is required")
        @ValidUUID
        private String productId;

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be positive")
        private Integer quantity;
    }
}