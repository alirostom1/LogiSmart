package io.github.alirostom1.logismart.dto.response.delivery;

import io.github.alirostom1.logismart.model.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryTrackingResponse {
    private String id;
    private String description;
    private DeliveryStatus currentStatus;
    private String destinationCity;
    private String recipientName;
    private String senderName;
    private LocalDateTime lastUpdate;
}