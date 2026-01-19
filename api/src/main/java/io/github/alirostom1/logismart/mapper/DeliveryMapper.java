// src/main/java/io/github/alirostom1/logismart/mapper/DeliveryMapper.java
package io.github.alirostom1.logismart.mapper;

import io.github.alirostom1.logismart.dto.request.delivery.CreateDeliveryRequest;
import io.github.alirostom1.logismart.dto.response.delivery.DeliveryResponse;
import io.github.alirostom1.logismart.dto.response.delivery.DeliveryDetailsResponse;
import io.github.alirostom1.logismart.dto.response.delivery.DeliveryTrackingResponse;
import io.github.alirostom1.logismart.model.entity.Delivery;
import io.github.alirostom1.logismart.model.enums.DeliveryPriority;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring",
        uses = {PersonMapper.class, ZoneMapper.class, DeliveryProductMapper.class, DeliveryHistoryMapper.class, SimpleCourierMapper.class})
public interface DeliveryMapper {
    //RESPONSE MAPPING
    @Mapping(target = "recipientName",source = "recipient.name")
    @Mapping(target = "senderName",
            expression = "java(delivery.getSender().getFirstName() + \" \" + delivery.getSender().getLastName())")
    @Mapping(target = "pickupZoneName", source = "pickupZone.name")
    @Mapping(target = "shippingZoneName", source = "shippingZone.name")
    @Mapping(target = "collectingCourierName",
            expression = "java(delivery.getCollectingCourier() != null ? delivery.getCollectingCourier().getFirstName() + \" \" + delivery.getCollectingCourier().getLastName() : null)")
    @Mapping(target = "shippingCourierName",
            expression = "java(delivery.getShippingCourier() != null ? delivery.getShippingCourier().getFirstName() + \" \" + delivery.getShippingCourier().getLastName() : null)")
    @Mapping(target = "weight",source = "weightKg")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    DeliveryResponse toResponse(Delivery delivery);

    @Mapping(target = "products", source = "deliveryProducts")
    @Mapping(target = "history", source = "deliveryHistoryList")
    @Mapping(target = "collectingCourier", source = "collectingCourier")
    @Mapping(target = "shippingCourier", source = "shippingCourier")
    @Mapping(target = "pickupZone", source = "pickupZone")
    @Mapping(target = "shippingZone", source = "shippingZone")
    @Mapping(target = "weight",source = "weightKg")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    DeliveryDetailsResponse toDetailsResponse(Delivery delivery);

    @Mapping(target = "currentStatus", source = "status")
    @Mapping(target = "recipientName",source = "recipient.name")
    @Mapping(target = "senderName",
            expression = "java(delivery.getSender().getFirstName() + \" \" + delivery.getSender().getLastName())")
    @Mapping(target = "collectingCourierName",
            expression = "java(delivery.getCollectingCourier() != null ? delivery.getCollectingCourier().getFirstName() + \" \" + delivery.getCollectingCourier().getLastName() : null)")
    @Mapping(target = "shippingCourierName",
            expression = "java(delivery.getShippingCourier() != null ? delivery.getShippingCourier().getFirstName() + \" \" + delivery.getShippingCourier().getLastName() : null)")
    @Mapping(target = "lastUpdate", source = "updatedAt")
    DeliveryTrackingResponse toTrackingResponse(Delivery delivery);


    List<DeliveryResponse> toResponseList(List<Delivery> deliveries);



    // REQUEST MAPPING
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "CREATED")
    @Mapping(target = "priority", source = "priority", qualifiedByName = "stringToPriority")
    @Mapping(target = "weightKg", source = "weight", qualifiedByName = "doubleToBigDecimal")
    @Mapping(target = "sender", ignore = true)
    @Mapping(target = "recipient", ignore = true)
    @Mapping(target = "pickupZone", ignore = true)
    @Mapping(target = "shippingZone", ignore = true)
    @Mapping(target = "collectingCourier", ignore = true)
    @Mapping(target = "shippingCourier", ignore = true)
    @Mapping(target = "deliveryProducts", ignore = true)
    @Mapping(target = "deliveryHistoryList", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "trackingNumber", ignore = true)
    Delivery toEntity(CreateDeliveryRequest request);

    @Named("stringToPriority")
    default DeliveryPriority stringToPriority(String priority) {
        if (priority == null || priority.isBlank()) {
            return DeliveryPriority.MEDIUM;
        }
        try {
            return DeliveryPriority.valueOf(priority.toUpperCase());
        } catch (IllegalArgumentException e) {
            return DeliveryPriority.MEDIUM;
        }
    }

    @Named("doubleToBigDecimal")
    default BigDecimal doubleToBigDecimal(Double value) {
        return value != null ? BigDecimal.valueOf(value) : BigDecimal.ZERO;
    }
}