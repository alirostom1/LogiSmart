package io.github.alirostom1.logismart.service;

import io.github.alirostom1.logismart.exception.CourierNotFoundException;
import io.github.alirostom1.logismart.exception.DeliveryNotFoundException;
import io.github.alirostom1.logismart.dto.deliverydto.CreateDeliveryDto;
import io.github.alirostom1.logismart.dto.deliverydto.UpdateDeliveryDto;
import io.github.alirostom1.logismart.model.entity.Courier;
import io.github.alirostom1.logismart.model.entity.Delivery;
import io.github.alirostom1.logismart.model.enums.DeliveryStatus;
import io.github.alirostom1.logismart.repository.CourierRepo;
import io.github.alirostom1.logismart.repository.DeliveryRepo;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
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
    public Delivery createDelivery(CreateDeliveryDto dto) {
        Courier courier = courierRepo.findById(UUID.fromString(dto.getCourrierID()))
                .orElseThrow(()-> new CourierNotFoundException(UUID.fromString(dto.getCourrierID())));
        Delivery delivery = new Delivery(
                dto.getRecipient(),
                dto.getWeight(),
                dto.getAddress(),
                DeliveryStatus.valueOf(dto.getStatus().toUpperCase())
        );
        delivery.setCourier(courier);
        return deliveryRepo.save(delivery);
    }

    @Override
    public Delivery updateDelivery(UpdateDeliveryDto dto) {
        Delivery delivery = deliveryRepo.findById(dto.getId())
                .orElseThrow(() -> new DeliveryNotFoundException(dto.getId()));
        delivery.setRecipient(dto.getRecipient());
        delivery.setAddress(dto.getAddress());
        delivery.setWeight(dto.getWeight());
        delivery.setStatus(DeliveryStatus.valueOf(dto.getStatus().toUpperCase()));
        if(!delivery.getCourier().getId().equals(dto.getCourrierID())){
            Courier courier = courierRepo.findById(UUID.fromString(dto.getCourrierID()))
                    .orElseThrow(()->new CourierNotFoundException(UUID.fromString(dto.getCourrierID())));
            delivery.setCourier(courier);
        }
        return deliveryRepo.save(delivery);
    }

    @Override
    public List<Delivery> getAllDeliveries() {
        return deliveryRepo.findAll();
    }

    @Override
    public Delivery getDeliveryById(UUID id) {
        return deliveryRepo.findById(id).orElseThrow(()-> new DeliveryNotFoundException(id));
    }

    @Override
    public void deleteDelivery(UUID id) {
        deliveryRepo.findById(id)
                .orElseThrow(() -> new DeliveryNotFoundException(id));
        deliveryRepo.deleteById(id);
    }

    @Override
    public Delivery updateStatus(UUID id,DeliveryStatus status){
        Delivery delivery = deliveryRepo.findById(id)
                .orElseThrow(()->new DeliveryNotFoundException(id));
        delivery.setStatus(status);
        return deliveryRepo.save(delivery);
    }
    @Override
    public List<Delivery> getDeliveriesByCourierId(UUID courierId){
        Courier courier = courierRepo.findById(courierId)
                .orElseThrow(()-> new CourierNotFoundException(courierId));
        return courier.getDeliveries();
    }
}
