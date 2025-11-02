package io.github.alirostom1.logismart.dto.response.courier;

import io.github.alirostom1.logismart.dto.response.delivery.DeliveryResponse;
import io.github.alirostom1.logismart.dto.response.zone.ZoneResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourierWithDeliveriesResponse {
    private String id;
    private String lastName;
    private String firstName;
    private String vehicle;
    private String phoneNumber;
    private ZoneResponse zone;
    private List<DeliveryResponse> assignedDeliveries;
    private int totalDeliveries;
    private int pendingDeliveries;
    private int completedDeliveries;
    private LocalDateTime createdAt;
}