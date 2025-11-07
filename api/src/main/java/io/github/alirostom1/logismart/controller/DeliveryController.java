package io.github.alirostom1.logismart.controller;

import io.github.alirostom1.logismart.dto.request.delivery.AssignDeliveryRequest;
import io.github.alirostom1.logismart.dto.request.delivery.CreateDeliveryRequest;
import io.github.alirostom1.logismart.dto.request.delivery.SearchDeliveryRequest;
import io.github.alirostom1.logismart.dto.request.delivery.UpdateDeliveryStatusRequest;
import io.github.alirostom1.logismart.dto.response.common.ApiResponse;
import io.github.alirostom1.logismart.dto.response.delivery.DeliveryDetailsResponse;
import io.github.alirostom1.logismart.dto.response.delivery.DeliveryResponse;
import io.github.alirostom1.logismart.dto.response.delivery.DeliveryTrackingResponse;
import io.github.alirostom1.logismart.service.DeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v2/deliveries")
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService deliveryService;


    @PostMapping
    public ResponseEntity<ApiResponse<DeliveryDetailsResponse>> createDelivery(@Valid @RequestBody CreateDeliveryRequest request) {
        DeliveryDetailsResponse response = deliveryService.createDelivery(request);
        ApiResponse<DeliveryDetailsResponse> apiResponse = new ApiResponse<>(
                true,
                "Delivery created successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
    @GetMapping("/{deliveryId}")
    public ResponseEntity<ApiResponse<DeliveryDetailsResponse>> getDeliveryById(@PathVariable String deliveryId) {
        DeliveryDetailsResponse response = deliveryService.getDeliveryById(deliveryId);
        ApiResponse<DeliveryDetailsResponse> apiResponse = new ApiResponse<>(
                true,
                "Delivery retrieved successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/{deliveryId}/tracking")
    public ResponseEntity<ApiResponse<DeliveryTrackingResponse>> trackDelivery(@PathVariable String deliveryId) {
        DeliveryTrackingResponse response = deliveryService.getDeliveryTracking(deliveryId);
        ApiResponse<DeliveryTrackingResponse> apiResponse = new ApiResponse<>(
                true,
                "Delivery tracking information retrieved successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(apiResponse);
    }
    @PatchMapping("/{deliveryId}/status")
    public ResponseEntity<ApiResponse<DeliveryDetailsResponse>> updateDeliveryStatus(
            @PathVariable String deliveryId,
            @Valid @RequestBody UpdateDeliveryStatusRequest request) {
        DeliveryDetailsResponse response = deliveryService.updateDeliveryStatus(deliveryId, request);
        ApiResponse<DeliveryDetailsResponse> apiResponse = new ApiResponse<>(
                true,
                "Delivery status updated successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(apiResponse);
    }
    @PatchMapping("/{deliveryId}/assign-collecting-courier")
    public ResponseEntity<ApiResponse<DeliveryDetailsResponse>> assignCollectingCourier(
            @PathVariable String deliveryId,
            @Valid @RequestBody AssignDeliveryRequest request) {
        DeliveryDetailsResponse response = deliveryService.assignCollectingCourier(deliveryId, request);
        ApiResponse<DeliveryDetailsResponse> apiResponse = new ApiResponse<>(
                true,
                "Collecting courier assigned successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(apiResponse);
    }
    @PatchMapping("/{deliveryId}/assign-shipping-courier")
    public ResponseEntity<ApiResponse<DeliveryDetailsResponse>> assignShippingCourier(
            @PathVariable String deliveryId,
            @Valid @RequestBody AssignDeliveryRequest request) {
        DeliveryDetailsResponse response = deliveryService.assignShippingCourier(deliveryId, request);
        ApiResponse<DeliveryDetailsResponse> apiResponse = new ApiResponse<>(
                true,
                "Shipping courier assigned successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<DeliveryResponse>>> searchDeliveries(
            @ParameterObject Pageable pageable,
            @Valid @RequestBody SearchDeliveryRequest request){
        Page<DeliveryResponse> responsePage = deliveryService.searchDeliveries(request,pageable);
        ApiResponse<Page<DeliveryResponse>> apiResponse = new ApiResponse<>(
                true,
                "Deliveries retrieved successfully!",
                responsePage,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/sender/{senderId}")
    public ResponseEntity<ApiResponse<Page<DeliveryResponse>>> getDeliveriesBySender(
            @PathVariable String senderId,
            @ParameterObject Pageable pageable) {

        Page<DeliveryResponse> response = deliveryService.getDeliveriesBySender(senderId, pageable);
        ApiResponse<Page<DeliveryResponse>> apiResponse = new ApiResponse<>(
                true,
                "Sender deliveries retrieved successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/recipient/{recipientId}")
    public ResponseEntity<ApiResponse<Page<DeliveryResponse>>> getDeliveriesByRecipient(
            @PathVariable String recipientId,
            @ParameterObject Pageable pageable) {

        Page<DeliveryResponse> response = deliveryService.getDeliveriesByRecipient(recipientId, pageable);
        ApiResponse<Page<DeliveryResponse>> apiResponse = new ApiResponse<>(
                true,
                "Recipient deliveries retrieved successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/courier/{courierId}")
    public ResponseEntity<ApiResponse<Page<DeliveryResponse>>> getDeliveriesByCourier(
            @PathVariable String courierId,
            @ParameterObject Pageable pageable) {

        Page<DeliveryResponse> response = deliveryService.getDeliveriesByCourier(courierId, pageable);
        ApiResponse<Page<DeliveryResponse>> apiResponse = new ApiResponse<>(
                true,
                "Courier deliveries retrieved successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(apiResponse);
    }
    @DeleteMapping("/{deliveryId}")
    public ResponseEntity<ApiResponse<Void>> deleteDelivery(@PathVariable String deliveryId) {
        deliveryService.deleteDelivery(deliveryId);
        ApiResponse<Void> apiResponse = new ApiResponse<>(
                true,
                "Delivery deleted successfully!",
                null,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(apiResponse);
    }
}
