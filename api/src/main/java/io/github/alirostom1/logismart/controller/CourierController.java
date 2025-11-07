package io.github.alirostom1.logismart.controller;

import io.github.alirostom1.logismart.dto.request.courier.CreateCourierRequest;
import io.github.alirostom1.logismart.dto.request.courier.UpdateCourierRequest;
import io.github.alirostom1.logismart.dto.response.common.ApiResponse;
import io.github.alirostom1.logismart.dto.response.courier.CourierResponse;
import io.github.alirostom1.logismart.dto.response.courier.CourierWithDeliveriesResponse;
import io.github.alirostom1.logismart.service.CourierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/couriers")
@RequiredArgsConstructor
public class CourierController {

    private final CourierService courierService;

    @PostMapping
    public ResponseEntity<ApiResponse<CourierResponse>> createCourier(@Valid @RequestBody CreateCourierRequest request) {
        CourierResponse response = courierService.createCourier(request);
        ApiResponse<CourierResponse> apiResponse = new ApiResponse<>(
                true,
                "Courier created successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/{courierId}")
    public ResponseEntity<ApiResponse<CourierResponse>> getCourierById(@PathVariable String courierId) {
        CourierResponse response = courierService.getCourierById(courierId);
        ApiResponse<CourierResponse> apiResponse = new ApiResponse<>(
                true,
                "Courier retrieved successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{courierId}/with-deliveries")
    public ResponseEntity<ApiResponse<CourierWithDeliveriesResponse>> getCourierWithDeliveries(@PathVariable String courierId) {
        CourierWithDeliveriesResponse response = courierService.getCourierWithDeliveries(courierId);
        ApiResponse<CourierWithDeliveriesResponse> apiResponse = new ApiResponse<>(
                true,
                "Courier with deliveries retrieved successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{courierId}")
    public ResponseEntity<ApiResponse<CourierResponse>> updateCourier(
            @PathVariable String courierId,
            @Valid @RequestBody UpdateCourierRequest request) {
        CourierResponse response = courierService.updateCourier(courierId, request);
        ApiResponse<CourierResponse> apiResponse = new ApiResponse<>(
                true,
                "Courier updated successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CourierResponse>>> getAllCouriers(@ParameterObject Pageable pageable) {
        Page<CourierResponse> response = courierService.getAllCouriers(pageable);
        ApiResponse<Page<CourierResponse>> apiResponse = new ApiResponse<>(
                true,
                "Couriers retrieved successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<ApiResponse<Page<CourierResponse>>> getCouriersByZone(
            @PathVariable String zoneId,
            @ParameterObject Pageable pageable) {
        Page<CourierResponse> response = courierService.getCouriersByZone(zoneId, pageable);
        ApiResponse<Page<CourierResponse>> apiResponse = new ApiResponse<>(
                true,
                "Couriers by zone retrieved successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{courierId}")
    public ResponseEntity<ApiResponse<Void>> deleteCourier(@PathVariable String courierId) {
        courierService.deleteCourier(courierId);
        ApiResponse<Void> apiResponse = new ApiResponse<>(
                true,
                "Courier deleted successfully!",
                null,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(apiResponse);
    }
}