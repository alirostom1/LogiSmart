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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
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
        return new Delivery();
    }

    @Override
    public Delivery updateDelivery(UpdateDeliveryDto dto) {
        return new Delivery();
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
