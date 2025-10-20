package io.github.alirostom1.smartlogi.service;

import io.github.alirostom1.smartlogi.model.entity.Courier;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourierService {
    Courier createCourier(String firstName, String lastName, String vehicle, String phone);
    Courier updateCourier(UUID id, String firstName, String lastName, String vehicle, String phone);
    List<Courier> getAllCouriers();
    Courier getCourierById(UUID id);
    void deleteCourierById(UUID id);

}
