package io.github.alirostom1.logismart.service;

import io.github.alirostom1.logismart.dto.courierdto.CreateCourierDto;
import io.github.alirostom1.logismart.dto.courierdto.UpdateCourierDto;
import io.github.alirostom1.logismart.model.entity.Courier;

import java.util.List;
import java.util.UUID;

public interface CourierService {
    Courier createCourier(CreateCourierDto dto);
    Courier updateCourier(UpdateCourierDto dto);
    List<Courier> getAllCouriers();
    Courier getCourierById(UUID id);
    void deleteCourierById(UUID id);
}
