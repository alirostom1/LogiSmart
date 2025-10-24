package io.github.alirostom1.logismart.service;

import io.github.alirostom1.logismart.dto.deliverydto.CreateDeliveryDto;
import io.github.alirostom1.logismart.dto.deliverydto.UpdateDeliveryDto;
import io.github.alirostom1.logismart.model.entity.Delivery;
import io.github.alirostom1.logismart.model.enums.DeliveryStatus;

import java.util.List;
import java.util.UUID;

public interface DeliveryService {
    Delivery createDelivery(CreateDeliveryDto dto);
    Delivery updateDelivery(UpdateDeliveryDto dto);
    List<Delivery> getAllDeliveries();
    Delivery getDeliveryById(UUID id);
    void deleteDelivery(UUID id);
    Delivery updateStatus(UUID id, DeliveryStatus status);
    List<Delivery> getDeliveriesByCourierId(UUID courierId);
}
