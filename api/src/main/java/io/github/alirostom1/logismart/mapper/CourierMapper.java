package io.github.alirostom1.logismart.mapper;

import io.github.alirostom1.logismart.dto.request.courier.CreateCourierRequest;
import io.github.alirostom1.logismart.dto.request.courier.UpdateCourierRequest;
import io.github.alirostom1.logismart.dto.response.courier.CourierResponse;
import io.github.alirostom1.logismart.dto.response.courier.CourierWithDeliveriesResponse;
import io.github.alirostom1.logismart.model.entity.Courier;
import io.github.alirostom1.logismart.model.entity.Delivery;
import io.github.alirostom1.logismart.model.enums.DeliveryStatus;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",uses = {ZoneMapper.class,DeliveryMapper.class})
public interface CourierMapper {

    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    CourierResponse toResponse(Courier courier);

    @Mapping(target = "totalDeliveries",
            expression = "java(courier.getCollectingDeliveries().size() + courier.getCollectingDeliveries().size())")
    @Mapping(target = "pendingDeliveries",
            expression = "java((int) courier.getCollectingDeliveries().stream().filter(d -> d.getStatus() != io.github.alirostom1.logismart.model.enums.DeliveryStatus.IN_STOCK).count() + " +
                    "(int)courier.getShippingDeliveries().stream().filter(d -> d.getStatus() != io.github.alirostom1.logismart.model.enums.DeliveryStatus.DELIVERED).count())")
    @Mapping(target = "completedDeliveries",
            expression = "java((int) courier.getCollectingDeliveries().stream().filter(d -> d.getStatus() == io.github.alirostom1.logismart.model.enums.DeliveryStatus.IN_STOCK).count() + " +
                    "(int)courier.getShippingDeliveries().stream().filter(d -> d.getStatus() == io.github.alirostom1.logismart.model.enums.DeliveryStatus.DELIVERED).count())")
    @Mapping(target = "createdAt", source = "createdAt")
    CourierWithDeliveriesResponse toWithDeliveriesResponse(Courier courier);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "collectingDeliveries", ignore = true)
    @Mapping(target = "shippingDeliveries", ignore = true)
    @Mapping(target = "password",ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "zone", ignore = true)
    Courier toEntity(CreateCourierRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "collectingDeliveries", ignore = true)
    @Mapping(target = "shippingDeliveries", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "zone", ignore = true)
    void updateFromRequest(UpdateCourierRequest request, @MappingTarget Courier courier);
    //WE USE THIS STYLE OF FUNCTION BECAUSE WE'RE MAPPING UPDATES TO AN EXISTING COURIER OBJECT(NOT A NEW ONE)
}