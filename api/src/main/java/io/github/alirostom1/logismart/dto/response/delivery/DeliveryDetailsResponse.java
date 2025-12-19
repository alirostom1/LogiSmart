package io.github.alirostom1.logismart.dto.response.delivery;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.alirostom1.logismart.dto.response.client.RecipientResponse;
import io.github.alirostom1.logismart.dto.response.client.SenderResponse;
import io.github.alirostom1.logismart.dto.response.courier.CourierResponse;
import io.github.alirostom1.logismart.dto.response.history.DeliveryHistoryResponse;
import io.github.alirostom1.logismart.dto.response.product.ProductInDeliveryResponse;
import io.github.alirostom1.logismart.dto.response.zone.ZoneResponse;
import io.github.alirostom1.logismart.model.enums.DeliveryPriority;
import io.github.alirostom1.logismart.model.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeliveryDetailsResponse {
    private String id;
    private String description;
    private String destinationCity;
    private double weight;
    private String status;
    private String priority;
    private SenderResponse sender;
    private RecipientResponse recipient;
    private CourierResponse collectingCourier;
    private CourierResponse shippingCourier;
    private ZoneResponse zone;
    private List<ProductInDeliveryResponse> products;
    private List<DeliveryHistoryResponse> history;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}