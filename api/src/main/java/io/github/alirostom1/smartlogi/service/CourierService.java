package io.github.alirostom1.smartlogi.service;

import io.github.alirostom1.smartlogi.model.entity.Courier;
import io.github.alirostom1.smartlogi.repository.CourierRepo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CourierService {
    private final CourierRepo courierRepo;

    public CourierService(CourierRepo courierRepo){
        this.courierRepo = courierRepo;
    }

    public Courier createCourier(String firstName,String lastName,String vehicle,String phone){
        if(courierRepo.existsByPhoneNumber(phone)){
            throw new RuntimeException("Courier with phone: " + phone + " already exists!");
        }
        Courier courier = new Courier(
                lastName,
                firstName,
                vehicle,
                phone
        );
        return courierRepo.save(courier);
    }
    public Courier updateCourier(UUID id,String firstName,String lastName,String vehicle,String phone){
        Courier courier = courierRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Courier with id : " + id + " not found!"));
        if(courierRepo.existsByPhoneNumberAndIdNot(phone,id)){
            throw new RuntimeException("Phone number: " + phone + " already bealongs to another courier!");
        }
        courier.setFirstName(firstName);
        courier.setLastName(lastName);
        courier.setVehicle(vehicle);
        courier.setPhoneNumber(phone);
        return courierRepo.save(courier);
    }
    public List<Courier> getAllCouriers(){
        return courierRepo.findAll();
    }
    public Optional<Courier> getCourierById(UUID id){
        return courierRepo.findById(id);
    }
    public void deleteCourierById(UUID id){
        if(!courierRepo.existsById(id)){
            throw new RuntimeException("Courier with id: " + id + " doesn't exist!");
        }
        courierRepo.deleteById(id);
    }

}
