package io.github.alirostom1.logismart.dto.request.delivery;

import io.github.alirostom1.logismart.model.enums.DeliveryPriority;
import io.github.alirostom1.logismart.model.enums.DeliveryStatus;
import io.github.alirostom1.logismart.util.ValidUUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SearchDeliveryRequest {

    private String searchTerm;

    @Pattern(regexp = "^(|CREATED|COLLECTED|IN_STOCK|IN_TRANSIT|DELIVERED|FAILED|CANCELLED)$",
            message = "Invalid status")
    private String status;

    @Pattern(regexp = "^(|LOW|MEDIUM|HIGH)$",
            message = "Invalid priority")
    private String priority;


    private Long pickupZoneId;

    private Long deliveryZoneId;

    private String city;

    @ValidUUID
    private String courierId;

    @ValidUUID
    private String senderId;


    @Pattern(regexp = "^\\+?\\d{10,15}$|^$", message = "Invalid phone format")
    private String phone;

}