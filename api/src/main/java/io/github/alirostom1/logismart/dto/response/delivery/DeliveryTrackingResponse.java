package io.github.alirostom1.logismart.dto.response.delivery;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.alirostom1.logismart.model.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeliveryTrackingResponse {
    private String id;
    private String description;
    private DeliveryStatus currentStatus;
    private String destinationCity;
    private String recipientName;
    private String senderName;
    private String collectingCourierName;
    private String shippingCourierName;
    private LocalDateTime lastUpdate;
}