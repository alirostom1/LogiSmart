package io.github.alirostom1.logismart.dto.response.client;

import io.github.alirostom1.logismart.dto.response.delivery.DeliveryResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SenderDeliveriesResponse {
    private String senderId;
    private String senderName;
    private String email;
    private String phone;
    private List<DeliveryResponse> deliveries;
    private int totalDeliveries;
    private int deliveredCount;
    private int inProgressCount;
}