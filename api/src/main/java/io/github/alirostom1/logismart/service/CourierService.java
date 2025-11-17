package io.github.alirostom1.logismart.service;

import io.github.alirostom1.logismart.dto.request.courier.CreateCourierRequest;
import io.github.alirostom1.logismart.dto.request.courier.UpdateCourierRequest;
import io.github.alirostom1.logismart.dto.response.courier.CourierResponse;
import io.github.alirostom1.logismart.dto.response.courier.CourierWithDeliveriesResponse;
import io.github.alirostom1.logismart.exception.EmailAlreadyExistsException;
import io.github.alirostom1.logismart.exception.PhoneAlreadyExistsException;
import io.github.alirostom1.logismart.exception.ResourceNotFoundException;
import io.github.alirostom1.logismart.mapper.CourierMapper;
import io.github.alirostom1.logismart.model.entity.Courier;
import io.github.alirostom1.logismart.model.entity.Person;
import io.github.alirostom1.logismart.model.entity.Zone;
import io.github.alirostom1.logismart.repository.PersonRepo;
import io.github.alirostom1.logismart.repository.ZoneRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CourierService {
    private final PersonRepo personRepository;
    private final CourierMapper courierMapper;
    private final ZoneRepo zoneRepo;

    // CREATE COURIER (FOR MANAGER)
    public CourierResponse createCourier(CreateCourierRequest request) {
        Zone zone = zoneRepo.findById(UUID.fromString(request.getZoneId()))
                .orElseThrow(() -> new ResourceNotFoundException("Zone", request.getZoneId()));

        if (personRepository.existsByPhoneAndTypeCourier(request.getPhone())) {
            throw new PhoneAlreadyExistsException(request.getPhone());
        }

        if (personRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        Courier courier = courierMapper.toEntity(request);
        courier.setZone(zone);
        Courier savedCourier = personRepository.save(courier);
        return courierMapper.toResponse(savedCourier);
    }

    // GET COURIER DETAILS (FOR MANAGER)
    @Transactional(readOnly = true)
    public CourierResponse getCourierById(String courierId) {
        Courier courier = findById(courierId);
        return courierMapper.toResponse(courier);
    }

    // GET COURIER WITH ASSIGNED DELIVERIES (FOR COURIER AND MANAGER)
    @Transactional(readOnly = true)
    public CourierWithDeliveriesResponse getCourierWithDeliveries(String courierId) {
        Courier courier = findById(courierId);
        return courierMapper.toWithDeliveriesResponse(courier);
    }

    // UPDATE COURIER INFORMATION (FOR MANAGER)
    public CourierResponse updateCourier(String courierId, UpdateCourierRequest request) {
        Courier courier = findById(courierId);
        Zone zone = zoneRepo.findById(UUID.fromString(request.getZoneId()))
                .orElseThrow(() -> new ResourceNotFoundException("Zone", request.getZoneId()));

        if (personRepository.existsByPhoneAndTypeCourierAndIdNot(request.getPhoneNumber(), UUID.fromString(courierId))) {
            throw new PhoneAlreadyExistsException(request.getPhoneNumber());
        }

        if (personRepository.existsByEmailAndIdNot(request.getEmail(), UUID.fromString(courierId))) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        courierMapper.updateFromRequest(request, courier);
        courier.setZone(zone);

        Courier updatedCourier = personRepository.save(courier);
        return courierMapper.toResponse(updatedCourier);
    }

    // GET ALL COURIERS WITH PAGINATION (FOR MANAGER)
    @Transactional(readOnly = true)
    public Page<CourierResponse> getAllCouriers(Pageable pageable) {
        Page<Courier> couriers = personRepository.findAllCouriers(pageable);
        return couriers.map(courierMapper::toResponse);
    }

    // GET COURIERS BY ZONE (FOR MANAGER)
    @Transactional(readOnly = true)
    public Page<CourierResponse> getCouriersByZone(String zoneId, Pageable pageable) {
        Page<Courier> couriers = personRepository.findCouriersByZoneId(UUID.fromString(zoneId), pageable);
        return couriers.map(courierMapper::toResponse);
    }

    // DELETE COURIER (FOR MANAGER)
    public void deleteCourier(String courierId) {
        if (!personRepository.existsByIdAndTypeCourier(UUID.fromString(courierId))) {
            throw new ResourceNotFoundException("Courier", courierId);
        }
        personRepository.deleteById(UUID.fromString(courierId));
    }

    // FIND COURIER BY ID AND THROW EXCEPTION IF NOT FOUND
    private Courier findById(String courierId) {
        return personRepository.findCourierById(UUID.fromString(courierId))
                .orElseThrow(() -> new ResourceNotFoundException("Courier", courierId));
    }
}