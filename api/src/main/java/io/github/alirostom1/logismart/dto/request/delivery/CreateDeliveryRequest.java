package io.github.alirostom1.logismart.dto.request.delivery;

import io.github.alirostom1.logismart.model.enums.DeliveryPriority;
import io.github.alirostom1.logismart.util.ValidUUID;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
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

    @NotBlank
    private String pickupAddress;

    @NotBlank
    private String pickupPostalCode;

    @NotBlank
    private String shippingAddress;

    @NotBlank
    private String shippingPostalCode;

    @NotNull
    private RecipientRequest recipientData;

    private List<DeliveryProductRequest> products;

    public static record RecipientRequest (
            String name,
            @Email(message = "Email must be valid")
            String email,
            @NotBlank(message = "Phone is required")
            String phone
    ) {
        public boolean isNewRecipient() {
            return name != null && !name.isBlank();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeliveryProductRequest {
        @NotNull(message = "Product ID is required")
        private Long productId;

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be positive")
        private Integer quantity;
    }
}