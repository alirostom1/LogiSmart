package io.github.alirostom1.logismart.dto.response.delivery;

import io.github.alirostom1.logismart.model.enums.DeliveryPriority;
import io.github.alirostom1.logismart.model.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryResponse {
    private String id;
    private String description;
    private String destinationCity;
    private double weight;
    private DeliveryStatus status;
    private DeliveryPriority priority;
    private String recipientName;
    private String senderName;
    private String zoneName;
    private String assignedCourierName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}