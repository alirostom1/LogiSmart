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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v3/couriers")
@Tag(name = "Courier API", description = "Endpoints for courier operations")
@RequiredArgsConstructor
public class CourierController {

    private final CourierService courierService;

    @PostMapping
    @PreAuthorize("hasAuthority('COURIER_SAVE')")
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
    @PreAuthorize("hasAuthority('COURIER_READ')")
    public ResponseEntity<DefaultApiResponse<CourierResponse>> getCourierById(
            @PathVariable Long courierId) {
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
    @PreAuthorize("hasAuthority('COURIER_READ') or hasAuthority('COURIER_READ_OWN')")
    public ResponseEntity<DefaultApiResponse<CourierWithDeliveriesResponse>> getCourierWithDeliveries(
            @PathVariable Long courierId) {
        CourierWithDeliveriesResponse response = courierService.getCourierWithDeliveries(courierId);
        DefaultApiResponse<CourierWithDeliveriesResponse> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Courier with deliveries retrieved successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }


    @PutMapping("/{courierId}")
    @PreAuthorize("hasAuthority('COURIER_SAVE')")
    public ResponseEntity<DefaultApiResponse<CourierResponse>> updateCourier(
            @PathVariable Long courierId,
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

    @GetMapping
    @PreAuthorize("hasAuthority('COURIER_READ')")
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
    @PreAuthorize("hasAuthority('COURIER_READ')")
    public ResponseEntity<DefaultApiResponse<Page<CourierResponse>>> getCouriersByZone(
            @PathVariable Long zoneId,
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
    @PreAuthorize("hasAuthority('COURIER_DELETE')")
    public ResponseEntity<DefaultApiResponse<Void>> deleteCourier(
            @PathVariable Long courierId) {
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