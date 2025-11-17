package io.github.alirostom1.logismart.controller;

import io.github.alirostom1.logismart.dto.request.courier.CreateCourierRequest;
import io.github.alirostom1.logismart.dto.request.courier.UpdateCourierRequest;
import io.github.alirostom1.logismart.dto.response.common.DefaultApiResponse;
import io.github.alirostom1.logismart.dto.response.courier.CourierResponse;
import io.github.alirostom1.logismart.dto.response.courier.CourierWithDeliveriesResponse;
import io.github.alirostom1.logismart.service.CourierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v3/couriers")
@Tag(name = "Courier API", description = "Endpoints for courier operations")
@RequiredArgsConstructor
public class CourierController {

    private final CourierService courierService;

    @Operation(summary = "Create a new courier")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Courier created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<DefaultApiResponse<CourierResponse>> createCourier(@Valid @RequestBody CreateCourierRequest request) {
        CourierResponse response = courierService.createCourier(request);
        DefaultApiResponse<CourierResponse> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Courier created successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(defaultApiResponse);
    }

    @Operation(summary = "Get courier by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Courier found"),
            @ApiResponse(responseCode = "404", description = "Courier not found")
    })
    @GetMapping("/{courierId}")
    public ResponseEntity<DefaultApiResponse<CourierResponse>> getCourierById(
            @Parameter(description = "Courier ID") @PathVariable String courierId) {
        CourierResponse response = courierService.getCourierById(courierId);
        DefaultApiResponse<CourierResponse> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Courier retrieved successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }

    @Operation(summary = "Get courier with deliveries")
    @ApiResponse(responseCode = "200", description = "Courier and deliveries retrieved successfully")
    @GetMapping("/{courierId}/with-deliveries")
    public ResponseEntity<DefaultApiResponse<CourierWithDeliveriesResponse>> getCourierWithDeliveries(
            @Parameter(description = "Courier ID") @PathVariable String courierId) {
        CourierWithDeliveriesResponse response = courierService.getCourierWithDeliveries(courierId);
        DefaultApiResponse<CourierWithDeliveriesResponse> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Courier with deliveries retrieved successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }

    @Operation(summary = "Update courier information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Courier updated successfully"),
            @ApiResponse(responseCode = "404", description = "Courier not found")
    })
    @PutMapping("/{courierId}")
    public ResponseEntity<DefaultApiResponse<CourierResponse>> updateCourier(
            @Parameter(description = "Courier ID") @PathVariable String courierId,
            @Valid @RequestBody UpdateCourierRequest request) {
        CourierResponse response = courierService.updateCourier(courierId, request);
        DefaultApiResponse<CourierResponse> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Courier updated successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }

    @Operation(summary = "Get all couriers with pagination")
    @ApiResponse(responseCode = "200", description = "Couriers retrieved successfully")
    @GetMapping
    public ResponseEntity<DefaultApiResponse<Page<CourierResponse>>> getAllCouriers(@ParameterObject Pageable pageable) {
        Page<CourierResponse> response = courierService.getAllCouriers(pageable);
        DefaultApiResponse<Page<CourierResponse>> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Couriers retrieved successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }

    @Operation(summary = "Get couriers in a specific zone")
    @ApiResponse(responseCode = "200", description = "Couriers in zone retrieved successfully")
    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<DefaultApiResponse<Page<CourierResponse>>> getCouriersByZone(
            @Parameter(description = "Zone ID") @PathVariable String zoneId,
            @ParameterObject Pageable pageable) {
        Page<CourierResponse> response = courierService.getCouriersByZone(zoneId, pageable);
        DefaultApiResponse<Page<CourierResponse>> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Couriers by zone retrieved successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }

    @Operation(summary = "Delete a courier")
    @ApiResponse(responseCode = "200", description = "Courier deleted successfully")
    @DeleteMapping("/{courierId}")
    public ResponseEntity<DefaultApiResponse<Void>> deleteCourier(
            @Parameter(description = "Courier ID") @PathVariable String courierId) {
        courierService.deleteCourier(courierId);
        DefaultApiResponse<Void> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Courier deleted successfully!",
                null,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }
}