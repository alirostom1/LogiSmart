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
import io.github.alirostom1.logismart.model.entity.Zone;
import io.github.alirostom1.logismart.repository.CourierRepo;
import io.github.alirostom1.logismart.repository.ZoneRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CourierService {
    private final CourierRepo courierRepo;
    private final CourierMapper courierMapper;
    private final ZoneRepo zoneRepo;

    // CREATE COURIER (FOR MANAGER)
    public CourierResponse createCourier(CreateCourierRequest request) {
        Zone zone = zoneRepo.findById(request.getZoneId())
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));

        if (courierRepo.existsByPhone(request.getPhone())) {
            throw new PhoneAlreadyExistsException(request.getPhone());
        }

        if (courierRepo.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        Courier courier = courierMapper.toEntity(request);
        courier.setZone(zone);
        Courier savedCourier = courierRepo.save(courier);
        return courierMapper.toResponse(savedCourier);
    }

    // GET COURIER DETAILS (FOR MANAGER)
    @Transactional(readOnly = true)
    public CourierResponse getCourierById(Long courierId) {
        Courier courier = findById(courierId);
        return courierMapper.toResponse(courier);
    }

    // GET COURIER WITH ASSIGNED DELIVERIES (FOR COURIER AND MANAGER)
    @Transactional(readOnly = true)
    public CourierWithDeliveriesResponse getCourierWithDeliveries(Long courierId) {
        Courier courier = findById(courierId);
        return courierMapper.toWithDeliveriesResponse(courier);
    }

    // UPDATE COURIER INFORMATION (FOR MANAGER)
    public CourierResponse updateCourier(Long courierId, UpdateCourierRequest request) {
        Courier courier = findById(courierId);
        Zone zone = zoneRepo.findById(request.getZoneId())
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));

        if (courierRepo.existsByPhoneAndIdNot(request.getPhoneNumber(), courierId)) {
            throw new PhoneAlreadyExistsException(request.getPhoneNumber());
        }

        if (courierRepo.existsByEmailAndIdNot(request.getEmail(),courierId)) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        courierMapper.updateFromRequest(request, courier);
        courier.setZone(zone);

        Courier updatedCourier = courierRepo.save(courier);
        return courierMapper.toResponse(updatedCourier);
    }

    // GET ALL COURIERS WITH PAGINATION (FOR MANAGER)
    @Transactional(readOnly = true)
    public Page<CourierResponse> getAllCouriers(Pageable pageable) {
        Page<Courier> couriers = courierRepo.findAll(pageable);
        return couriers.map(courierMapper::toResponse);
    }

    // GET COURIERS BY ZONE (FOR MANAGER)
    @Transactional(readOnly = true)
    public Page<CourierResponse> getCouriersByZone(Long zoneId, Pageable pageable) {
        Zone zone = zoneRepo.findById(zoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found!"));
        Page<Courier> couriers = courierRepo.findCouriersByZone(zone, pageable);
        return couriers.map(courierMapper::toResponse);
    }

    // DELETE COURIER (FOR MANAGER)
    public void deleteCourier(Long courierId) {
        if (!courierRepo.existsById(courierId)) {
            throw new ResourceNotFoundException("Courier not found");
        }
        courierRepo.deleteById(courierId);
    }

    // FIND COURIER BY ID AND THROW EXCEPTION IF NOT FOUND
    private Courier findById(Long courierId) {
        return courierRepo.findById(courierId)
                .orElseThrow(() -> new ResourceNotFoundException("Courier not found"));
    }
}