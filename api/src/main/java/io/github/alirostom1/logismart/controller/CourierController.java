package io.github.alirostom1.logismart.controller;


import io.github.alirostom1.logismart.dto.ApiResponse;
import io.github.alirostom1.logismart.dto.ApiResponseSuccess;
import io.github.alirostom1.logismart.dto.courierdto.CreateCourierDto;
import io.github.alirostom1.logismart.dto.courierdto.UpdateCourierDto;
import io.github.alirostom1.logismart.model.entity.Courier;
import io.github.alirostom1.logismart.service.CourierService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v2/couriers")
public class CourierController{
    private CourierService courierService;

    public CourierController(CourierService courierService){
        this.courierService = courierService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> index(){
        List<Courier> couriers = courierService.getAllCouriers();
        ApiResponseSuccess<List<Courier>> response = new ApiResponseSuccess<>(
                200,
                "Couriers retrived successfully!",
                couriers
            );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> show(@PathVariable("id") UUID id){
        Courier courier = courierService.getCourierById(id);

        ApiResponseSuccess<Courier> response = new ApiResponseSuccess<>(
                200,
                "Successfully retrieved courier!",
                courier
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> store(@Valid @RequestBody CreateCourierDto courierDto){
        Courier courier = courierService.createCourier(courierDto);
        ApiResponseSuccess<Courier> response = new ApiResponseSuccess<>(
                201,
                "Courier created Successfully!",
                courier
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable("id") UUID id,@Valid @RequestBody UpdateCourierDto dto){
        dto.setId(id);
        Courier savedCourier = courierService.updateCourier(dto);
        ApiResponseSuccess<Courier> response = new ApiResponseSuccess(
                201,
                "Courier updated successfully!",
                savedCourier
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") UUID id){
        courierService.deleteCourierById(id);
        ApiResponse response = new ApiResponse(
                200,
                "Courier deleted successfully!"
        );
        return ResponseEntity.ok(response);
    }

}
