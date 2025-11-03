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

    CourierResponse toResponse(Courier courier);

    @Mapping(target = "assignedDeliveries", source = "deliveries")
    @Mapping(target = "totalDeliveries",
            expression = "java(courier.getDeliveries().size())")
    @Mapping(target = "pendingDeliveries",
            expression = "java((int) courier.getDeliveries().stream().filter(d -> d.getStatus() != io.github.alirostom1.logismart.model.enums.DeliveryStatus.DELIVERED).count())")
    @Mapping(target = "completedDeliveries",
            expression = "java((int) courier.getDeliveries().stream().filter(d -> d.getStatus() == io.github.alirostom1.logismart.model.enums.DeliveryStatus.DELIVERED).count())")
    CourierWithDeliveriesResponse toWithDeliveriesResponse(Courier courier);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deliveries", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "zone", ignore = true)
    Courier toEntity(CreateCourierRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deliveries", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "zone", ignore = true)
    void updateFromRequest(UpdateCourierRequest request, @MappingTarget Courier courier);
    //WE USE THIS STYLE OF FUNCTION BECAUSE WE'RE MAPPING UPDATES TO AN EXISTING COURIER OBJECT(NOT A NEW ONE)
}