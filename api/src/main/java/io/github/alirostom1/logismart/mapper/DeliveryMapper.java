// src/main/java/io/github/alirostom1/logismart/mapper/DeliveryMapper.java
package io.github.alirostom1.logismart.mapper;

import io.github.alirostom1.logismart.dto.request.delivery.CreateDeliveryRequest;
import io.github.alirostom1.logismart.dto.response.delivery.DeliveryResponse;
import io.github.alirostom1.logismart.dto.response.delivery.DeliveryDetailsResponse;
import io.github.alirostom1.logismart.dto.response.delivery.DeliveryTrackingResponse;
import io.github.alirostom1.logismart.model.entity.Delivery;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {PersonMapper.class, ZoneMapper.class, DeliveryProductMapper.class, DeliveryHistoryMapper.class, SimpleCourierMapper.class})
public interface DeliveryMapper {
    //RESPONSE MAPPING
    @Mapping(target = "recipientName",
            expression = "java(delivery.getRecipient().getFirstName() + \" \" + delivery.getRecipient().getLastName())")
    @Mapping(target = "senderName",
            expression = "java(delivery.getSender().getFirstName() + \" \" + delivery.getSender().getLastName())")
    @Mapping(target = "zoneName", source = "zone.name")
    @Mapping(target = "collectingCourierName",
            expression = "java(delivery.getCollectingCourier() != null ? delivery.getCollectingCourier().getFirstName() + \" \" + delivery.getCollectingCourier().getLastName() : null)")
    @Mapping(target = "shippingCourierName",
            expression = "java(delivery.getShippingCourier() != null ? delivery.getShippingCourier().getFirstName() + \" \" + delivery.getShippingCourier().getLastName() : null)")
    DeliveryResponse toResponse(Delivery delivery);

    @Mapping(target = "products", source = "deliveryProducts")
    @Mapping(target = "history", source = "deliveryHistoryList")
    @Mapping(target = "collectingCourier", source = "collectingCourier")
    @Mapping(target = "shippingCourier", source = "shippingCourier")
    DeliveryDetailsResponse toDetailsResponse(Delivery delivery);

    @Mapping(target = "currentStatus", source = "status")
    @Mapping(target = "recipientName",
            expression = "java(delivery.getRecipient().getFirstName() + \" \" + delivery.getRecipient().getLastName())")
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
    @Mapping(target = "sender", ignore = true)
    @Mapping(target = "recipient", ignore = true)
    @Mapping(target = "zone", ignore = true)
    @Mapping(target = "collectingCourier", ignore = true)
    @Mapping(target = "shippingCourier", ignore = true)
    @Mapping(target = "deliveryProducts", ignore = true)
    @Mapping(target = "deliveryHistoryList", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Delivery toEntity(CreateDeliveryRequest request);
}