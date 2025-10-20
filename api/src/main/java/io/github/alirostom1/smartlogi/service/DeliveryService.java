package io.github.alirostom1.smartlogi.service;

import io.github.alirostom1.smartlogi.model.entity.Delivery;
import io.github.alirostom1.smartlogi.model.enums.DeliveryStatus;

import java.util.List;
import java.util.UUID;

public interface DeliveryService {
    Delivery createDelivery(String recipient, String address, double weight, UUID courierId);
    Delivery updateDelivery(UUID id,String recipient, String address, double weight,UUID courierID);
    List<Delivery> getAllDeliveries();
    Delivery getDeliveryById(UUID id);
    void deleteDelivery(UUID id);
    Delivery markDeliveryAsInTransit(UUID id);
    Delivery markDeliveryAsDelivered(UUID id);
    Delivery markDeliveryAsPreparation(UUID id);
}
