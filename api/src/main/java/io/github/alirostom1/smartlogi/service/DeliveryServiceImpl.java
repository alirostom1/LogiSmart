package io.github.alirostom1.smartlogi.service;

import io.github.alirostom1.smartlogi.model.entity.Courier;
import io.github.alirostom1.smartlogi.model.entity.Delivery;
import io.github.alirostom1.smartlogi.model.enums.DeliveryStatus;
import io.github.alirostom1.smartlogi.repository.CourierRepo;
import io.github.alirostom1.smartlogi.repository.DeliveryRepo;

import java.util.List;
import java.util.UUID;

public class DeliveryServiceImpl implements DeliveryService{
    private DeliveryRepo deliveryRepo;
    private CourierRepo courierRepo;

    public void setCourierRepo(CourierRepo courierRepo) {
        this.courierRepo = courierRepo;
    }
    public void setDeliveryRepo(DeliveryRepo deliveryRepo) {
        this.deliveryRepo = deliveryRepo;
    }

    @Override
    public Delivery createDelivery(String recipient, String address, double weight, UUID courierId) {
        Courier courier = courierRepo.findById(courierId)
                .orElseThrow(()-> new RuntimeException("Courier with id: " + courierId + " doesn't exist!"));
        Delivery delivery = new Delivery(recipient,weight,address);
        delivery.setCourier(courier);
        return deliveryRepo.save(delivery);
    }

    @Override
    public Delivery updateDelivery(UUID id, String recipient, String address, double weight, UUID courierID) {
        Delivery delivery = deliveryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery with id: " + id + " deosn't exist!"));
        delivery.setRecipient(recipient);
        delivery.setAddress(address);
        delivery.setWeight(weight);
        if(!delivery.getId().equals(courierID)){
            Courier courier = courierRepo.findById(courierID)
                    .orElseThrow(()->new RuntimeException("Courier with id: " + id + " doesn't exist!"));
        }
        return deliveryRepo.save(delivery);
    }

    @Override
    public List<Delivery> getAllDeliveries() {
        return deliveryRepo.findAll();
    }

    @Override
    public Delivery getDeliveryById(UUID id) {
        return deliveryRepo.findById(id).orElseThrow(()-> new RuntimeException("Delivery with id: " + id + " doesn't exist"));
    }

    @Override
    public void deleteDelivery(UUID id) {
        if(!deliveryRepo.existsById(id)){
            throw new RuntimeException("Delivery with id: " + id + "doesn't exist");
        }
        deliveryRepo.deleteById(id);
    }

    @Override
    public Delivery markDeliveryAsInTransit(UUID id) {
        Delivery delivery = deliveryRepo.findById(id)
                .orElseThrow(()->new RuntimeException("Delivery with id: " + id + "doesn't exist"));
        delivery.setStatus(DeliveryStatus.IN_TRANSIT);
        return deliveryRepo.save(delivery);
    }

    @Override
    public Delivery markDeliveryAsDelivered(UUID id) {
        Delivery delivery = deliveryRepo.findById(id)
                .orElseThrow(()->new RuntimeException("Delivery with id: " + id + "doesn't exist"));
        delivery.setStatus(DeliveryStatus.DELIVERED);
        return deliveryRepo.save(delivery);
    }

    @Override
    public Delivery markDeliveryAsPreparation(UUID id) {
        Delivery delivery = deliveryRepo.findById(id)
                .orElseThrow(()->new RuntimeException("Delivery with id: " + id + "doesn't exist"));
        delivery.setStatus(DeliveryStatus.PREPARATION);
        return deliveryRepo.save(delivery);
    }
}
