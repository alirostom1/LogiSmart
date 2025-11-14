package io.github.alirostom1.logismart.service;

import io.github.alirostom1.logismart.dto.request.courier.CreateCourierRequest;
import io.github.alirostom1.logismart.dto.request.courier.UpdateCourierRequest;
import io.github.alirostom1.logismart.dto.response.courier.CourierResponse;
import io.github.alirostom1.logismart.dto.response.courier.CourierWithDeliveriesResponse;
import io.github.alirostom1.logismart.exception.PhoneAlreadyExistsException;
import io.github.alirostom1.logismart.exception.ResourceNotFoundException;
import io.github.alirostom1.logismart.mapper.CourierMapper;
import io.github.alirostom1.logismart.mapper.ZoneMapper;
import io.github.alirostom1.logismart.model.entity.Courier;
import io.github.alirostom1.logismart.model.entity.Zone;
import io.github.alirostom1.logismart.repository.CourierRepo;
import io.github.alirostom1.logismart.repository.ZoneRepo;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CourierServiceUnitTest{

    @Mock CourierRepo courierRepo;
    @Mock ZoneRepo zoneRepo;

    @Autowired
    CourierMapper courierMapper;

    CourierService courierService;

    @BeforeEach
    void setup(){
        courierService = new CourierService(courierRepo,courierMapper,zoneRepo);
    }
    @Test
    public void createCourier_should_return_courier(){
        String zoneId = UUID.randomUUID().toString();
        Zone zone = new Zone();
        CreateCourierRequest request = new CreateCourierRequest("courier1","courier1","bmw","061237623",zoneId);

        Courier courier = courierMapper.toEntity(request);
        courier.setZone(zone);
        CourierResponse expectedResponse = courierMapper.toResponse(courier);
        when(zoneRepo.findById(UUID.fromString(zoneId))).thenReturn(Optional.of(zone));
        when(courierRepo.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(false);
        when(courierRepo.save(any(Courier.class))).thenReturn(courier);

        CourierResponse response = courierService.createCourier(request);
        assertEquals(expectedResponse,response);
    }
    @Test
    public void createCourier_should_throw_zone_not_found(){
        String zoneId = UUID.randomUUID().toString();
        CreateCourierRequest request = new CreateCourierRequest("courier1","courier1","bmw","061237623",zoneId);

        when(zoneRepo.findById(UUID.fromString(zoneId))).thenReturn(Optional.empty());

        Exception exception =assertThrows(ResourceNotFoundException.class,() -> courierService.createCourier(request));
        assertTrue(exception.getMessage().contains("Zone"));
    }
    @Test
    public void createCourier_should_throw_phone_already_exists(){
        String zoneId = UUID.randomUUID().toString();
        CreateCourierRequest request = new CreateCourierRequest("courier1","courier1","bmw","061237623",zoneId);
        Zone zone = new Zone();
        when(zoneRepo.findById(UUID.fromString(zoneId))).thenReturn(Optional.of(zone));
        when(courierRepo.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(true);
        Exception exception =assertThrows(PhoneAlreadyExistsException.class,() -> courierService.createCourier(request));
        assertTrue(exception.getMessage().contains(request.getPhoneNumber()));
    }

    //GET COURIER
    @Test
    public void getCourierById_should_return_courier(){
        UUID courierId = UUID.randomUUID();
        Courier courier = new Courier();
        when(courierRepo.findById(courierId)).thenReturn(Optional.of(courier));
        CourierResponse expectedResponse = courierMapper.toResponse(courier);
        CourierResponse response = courierService.getCourierById(courierId.toString());

        assertEquals(expectedResponse,response);
    }
    @Test
    public void getCourierById_should_throw_not_found(){
        UUID courierId = UUID.randomUUID();
        when(courierRepo.findById(courierId)).thenReturn(Optional.empty());
        Exception ex = assertThrows(ResourceNotFoundException.class,() -> courierService.getCourierById(courierId.toString()));
        assertTrue(ex.getMessage().contains("Courier"));
    }
    //GET COURIER WITH DELIVERIES
    @Test
    public void getCourierWithDeliveries_should_return_courier_with_deliveries(){
        UUID courierId = UUID.randomUUID();
        Courier courier = new Courier();
        when(courierRepo.findById(courierId)).thenReturn(Optional.of(courier));
        CourierWithDeliveriesResponse expectedResponse = courierMapper.toWithDeliveriesResponse(courier);
        CourierWithDeliveriesResponse response = courierService.getCourierWithDeliveries(courierId.toString());
        assertEquals(expectedResponse,response);
    }
    @Test
    public void getCourierWithDeliveries_should_throw_not_found(){
        UUID courierId = UUID.randomUUID();
        when(courierRepo.findById(courierId)).thenReturn(Optional.empty());
        Exception ex = assertThrows(ResourceNotFoundException.class,() -> courierService.getCourierWithDeliveries(courierId.toString()));
        assertTrue(ex.getMessage().contains("Courier"));
    }

    //UPDATE COURIER
    @Test
    public void updateCourier_should_return_courier(){
        UUID courierId = UUID.randomUUID();
        UUID zoneId = UUID.randomUUID();

        UpdateCourierRequest request = new UpdateCourierRequest("courier2",
                "courier2",
                "bmw",
                "0612376",
                zoneId.toString()
        );
        Courier courier = new Courier();
        courier.setId(courierId);

        Zone oldZone = new Zone();
        oldZone.setId(UUID.randomUUID());
        courier.setZone(oldZone);

        Zone newZone = new Zone();
        newZone.setId(zoneId);

        when(courierRepo.findById(courierId)).thenReturn(Optional.of(courier));
        when(zoneRepo.findById(zoneId)).thenReturn(Optional.of(newZone));
        when(courierRepo.existsByPhoneNumberAndIdNot(request.getPhoneNumber(), courierId)).thenReturn(false);
        when(courierRepo.save(any(Courier.class))).thenReturn(courier);

        CourierResponse response = courierService.updateCourier(courierId.toString(),request);

        courierMapper.updateFromRequest(request,courier);
        courier.setZone(newZone);
        CourierResponse expectedResponse = courierMapper.toResponse(courier);
        assertEquals(expectedResponse,response);
    }
    @Test
    public void updateCourier_should_throw_courier_not_found(){
        UUID courierId = UUID.randomUUID();

        UpdateCourierRequest request = new UpdateCourierRequest();
        when(courierRepo.findById(courierId)).thenReturn(Optional.empty());
        Exception ex = assertThrows(ResourceNotFoundException.class,() -> courierService.updateCourier(courierId.toString(),request));
        assertTrue(ex.getMessage().contains("Courier"));

    }
    @Test
    public void updateCourier_should_throw_zone_not_found(){
        UUID courierId = UUID.randomUUID();
        UUID zoneId = UUID.randomUUID();
        UpdateCourierRequest request = new UpdateCourierRequest();
        request.setZoneId(zoneId.toString());
        Courier courier = new Courier();
        when(courierRepo.findById(courierId)).thenReturn(Optional.of(courier));
        when(zoneRepo.findById(zoneId)).thenReturn(Optional.empty());
        Exception ex = assertThrows(ResourceNotFoundException.class,() -> courierService.updateCourier(courierId.toString(),request));
        assertTrue(ex.getMessage().contains("Zone"));
    }

    @Test
    public void updateCourier_should_throw_phone_already_exists(){
        UUID courierId = UUID.randomUUID();
        UUID zoneId = UUID.randomUUID();
        UpdateCourierRequest request = new UpdateCourierRequest();
        request.setZoneId(zoneId.toString());
        request.setPhoneNumber("0612737");
        Zone zone = new Zone();
        Courier courier = new Courier();
        when(courierRepo.findById(courierId)).thenReturn(Optional.of(courier));
        when(zoneRepo.findById(zoneId)).thenReturn(Optional.of(zone));
        when(courierRepo.existsByPhoneNumberAndIdNot(request.getPhoneNumber(),courierId)).thenReturn(true);
        assertThrows(PhoneAlreadyExistsException.class,
                () -> courierService.updateCourier(courierId.toString(),request)
        );
    }

    @Test
    public void getAllCouriers_should_return_page(){
        Pageable pageable = PageRequest.of(0, 10);
        Courier courier = new Courier();
        Courier courier1 = new Courier();
        Page<Courier> couriersPage = new PageImpl<>(
                List.of(courier1,courier),pageable,2
        );
        Page<CourierResponse> expectedResponse = couriersPage.map(courierMapper::toResponse);
        when(courierRepo.findAll(pageable)).thenReturn(couriersPage);
        Page<CourierResponse> response = courierService.getAllCouriers(pageable);
        assertEquals(expectedResponse,response);
    }
    @Test
    public void getCouriersByZone_should_return_page(){
        UUID zoneId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0,10);
        Courier courier1 = new Courier();
        Courier courier2 = new Courier();
        Zone zone = new Zone();
        Page<Courier> couriersPage = new PageImpl<>(
                List.of(courier1,courier2),pageable,2
        );
        Page<CourierResponse> expectedResponse = couriersPage.map(courierMapper::toResponse);

        when(courierRepo.findByZoneId(zoneId,pageable)).thenReturn(couriersPage);

        Page<CourierResponse> response = courierService.getCouriersByZone(zoneId.toString(),pageable);
        assertEquals(expectedResponse,response);
    }
    @Test
    public void deleteCourier_should_run(){
        UUID courierId = UUID.randomUUID();
        when(courierRepo.existsById(courierId)).thenReturn(true);
        courierService.deleteCourier(courierId.toString());
        verify(courierRepo).deleteById(courierId);
    }
    @Test
    public void deleteCourier_should_throw_not_found(){
        UUID courierId = UUID.randomUUID();
        when(courierRepo.existsById(courierId)).thenReturn(false);
        Exception ex = assertThrows(ResourceNotFoundException.class,() -> courierService.deleteCourier(courierId.toString()));
        assertTrue(ex.getMessage().contains("Courier"));
    }
}
