package io.github.alirostom1.logismart.service;

import io.github.alirostom1.logismart.exception.CourierNotFoundException;
import io.github.alirostom1.logismart.exception.PhoneAlreadyExistsException;
import io.github.alirostom1.logismart.dto.courierdto.CreateCourierDto;
import io.github.alirostom1.logismart.dto.courierdto.UpdateCourierDto;
import io.github.alirostom1.logismart.model.entity.Courier;
import io.github.alirostom1.logismart.repository.CourierRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CourierServiceImpl implements CourierService {
    private CourierRepo courierRepo;

    public CourierServiceImpl(CourierRepo courierRepo){
        this.courierRepo = courierRepo;
    }

    public Courier createCourier(CreateCourierDto dto){

        if(courierRepo.existsByPhoneNumber(dto.getPhone())){
            throw new PhoneAlreadyExistsException(dto.getPhone());
        }
        Courier courier = new Courier(
                dto.getLastName(),
                dto.getFirstName(),
                dto.getVehicle(),
                dto.getPhone()
        );
        return courierRepo.save(courier);
    }
    public Courier updateCourier(UpdateCourierDto dto){
        Courier courier = courierRepo.findById(dto.getId())
                .orElseThrow(() -> new CourierNotFoundException(dto.getId()));
        if(courierRepo.existsByPhoneNumberAndIdNot(dto.getPhone(),dto.getId())){
            throw new PhoneAlreadyExistsException(dto.getPhone());
        }
        courier.setFirstName(dto.getFirstName());
        courier.setLastName(dto.getLastName());
        courier.setVehicle(dto.getVehicle());
        courier.setPhoneNumber(dto.getPhone());
        return courierRepo.save(courier);
    }
    public List<Courier> getAllCouriers(){
        return courierRepo.findAll();
    }
    public Courier getCourierById(UUID id){
        return courierRepo.findById(id)
                .orElseThrow(() -> new CourierNotFoundException(id));
    }
    public void deleteCourierById(UUID id){
        if(!courierRepo.existsById(id)){
            throw new CourierNotFoundException(id);
        }
        courierRepo.deleteById(id);
    }

}
