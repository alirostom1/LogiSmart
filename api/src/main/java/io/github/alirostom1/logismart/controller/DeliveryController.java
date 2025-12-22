package io.github.alirostom1.logismart.controller;

import io.github.alirostom1.logismart.dto.request.delivery.AssignDeliveryRequest;
import io.github.alirostom1.logismart.dto.request.delivery.CreateDeliveryRequest;
import io.github.alirostom1.logismart.dto.request.delivery.SearchDeliveryRequest;
import io.github.alirostom1.logismart.dto.request.delivery.UpdateDeliveryStatusRequest;
import io.github.alirostom1.logismart.dto.response.common.DefaultApiResponse;
import io.github.alirostom1.logismart.dto.response.delivery.DeliveryDetailsResponse;
import io.github.alirostom1.logismart.dto.response.delivery.DeliveryResponse;
import io.github.alirostom1.logismart.dto.response.delivery.DeliveryTrackingResponse;
import io.github.alirostom1.logismart.model.entity.User;
import io.github.alirostom1.logismart.service.DeliveryService;
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
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v3/deliveries")
@Tag(name = "Delivery API", description = "Operations related to deliveries")
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService deliveryService;


    @PostMapping
    @PreAuthorize("hasAuthority('DELIVERY_SAVE')")
    public ResponseEntity<DefaultApiResponse<DeliveryDetailsResponse>> createDelivery(@Valid @RequestBody CreateDeliveryRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        DeliveryDetailsResponse response = deliveryService.createDelivery(request,user.getId());
        DefaultApiResponse<DeliveryDetailsResponse> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Delivery created successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(defaultApiResponse);
    }


    @Operation(summary = "Get delivery by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Delivery retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Delivery not found")
    })
    @GetMapping("/{deliveryId}")
    @PreAuthorize("hasAuthority('DELIVERY_READ')")
    public ResponseEntity<DefaultApiResponse<DeliveryDetailsResponse>> getDeliveryById(
            @Parameter(description = "Delivery ID")  @PathVariable Long deliveryId) {
        DeliveryDetailsResponse response = deliveryService.getDeliveryById(deliveryId);
        DefaultApiResponse<DeliveryDetailsResponse> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Delivery retrieved successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }
//
    @Operation(summary = "Get delivery tracking information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Delivery tracking information retrieved successfully!"),
            @ApiResponse(responseCode = "404", description = "Delivery not found")
    })
    @GetMapping("/tracking/{trackingNumber}")
    public ResponseEntity<DefaultApiResponse<DeliveryTrackingResponse>> trackDelivery(
            @Parameter(description = "Tracking Number") @PathVariable String trackingNumber) {
        DeliveryTrackingResponse response = deliveryService.getDeliveryTracking(trackingNumber);
        DefaultApiResponse<DeliveryTrackingResponse> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Delivery tracking information retrieved successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }
//
    @Operation(summary = "Update delivery status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Delivery status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Delivery not found")
    })
    @PatchMapping("/{deliveryId}/status")
    @PreAuthorize("hasAuthority('DELIVERY_UPDATE_STATUS')")
    public ResponseEntity<DefaultApiResponse<DeliveryDetailsResponse>> updateDeliveryStatus(
            @Parameter(description = "Delivery ID") @PathVariable Long deliveryId,
            @Valid @RequestBody UpdateDeliveryStatusRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        DeliveryDetailsResponse response = deliveryService.updateDeliveryStatus(deliveryId, request, user.getId());
        DefaultApiResponse<DeliveryDetailsResponse> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Delivery status updated successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }
//
    @Operation(summary = "Assign collecting courier")
    @ApiResponse(responseCode = "200", description = "Collecting courier assigned successfully")
    @PatchMapping("/{deliveryId}/assign-collecting-courier")
    @PreAuthorize("hasAuthority('DELIVERY_ASSIGN')")
    public ResponseEntity<DefaultApiResponse<DeliveryDetailsResponse>> assignCollectingCourier(
            @Parameter(description = "Delivery ID") @PathVariable Long deliveryId,
            @Valid @RequestBody AssignDeliveryRequest request) {
        DeliveryDetailsResponse response = deliveryService.assignCollectingCourier(deliveryId, request);
        DefaultApiResponse<DeliveryDetailsResponse> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Collecting courier assigned successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }

    @Operation(summary = "Assign shipping courier")
    @ApiResponse(responseCode = "200", description = "Shipping courier assigned successfully")
    @PatchMapping("/{deliveryId}/assign-shipping-courier")
    @PreAuthorize("hasAuthority('DELIVERY_ASSIGN')")
    public ResponseEntity<DefaultApiResponse<DeliveryDetailsResponse>> assignShippingCourier(
            @Parameter(description = "Delivery ID") @PathVariable Long deliveryId,
            @Valid @RequestBody AssignDeliveryRequest request) {
        DeliveryDetailsResponse response = deliveryService.assignShippingCourier(deliveryId, request);
        DefaultApiResponse<DeliveryDetailsResponse> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Shipping courier assigned successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }
//
    @Operation(summary = "Search deliveries (paginated)")
    @ApiResponse(responseCode = "200", description = "Deliveries retrieved successfully")
    @PostMapping("/search")
    @PreAuthorize("hasAuthority('DELIVERY_READ')")
    public ResponseEntity<DefaultApiResponse<Page<DeliveryResponse>>> searchDeliveries(
            @ParameterObject Pageable pageable,
            @Valid @RequestBody SearchDeliveryRequest request){
        Page<DeliveryResponse> responsePage = deliveryService.searchDeliveries(request,pageable);
        DefaultApiResponse<Page<DeliveryResponse>> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Deliveries retrieved successfully!",
                responsePage,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }

    @Operation(summary = "Get deliveries by sender")
    @ApiResponse(responseCode = "200", description = "Sender deliveries retrieved successfully")
    @GetMapping("/sender/{senderId}")
    @PreAuthorize("hasAuthority('DELIVERY_READ_OWN') or hasAuthority('DELIVERY_READ')")
    public ResponseEntity<DefaultApiResponse<Page<DeliveryResponse>>> getDeliveriesBySender(
            @Parameter(description = "Sender ID") @PathVariable Long senderId,
            @ParameterObject Pageable pageable) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Senders can only see their own deliveries
        if (user.getRole().getName().name().equals("ROLE_SENDER") && !user.getId().equals(senderId)) {
            throw new org.springframework.security.access.AccessDeniedException("Access denied");
        }
        Page<DeliveryResponse> response = deliveryService.getDeliveriesBySender(senderId, pageable);
        DefaultApiResponse<Page<DeliveryResponse>> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Sender deliveries retrieved successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }

    @Operation(summary = "Get deliveries by recipient")
    @ApiResponse(responseCode = "200", description = "Recipient deliveries retrieved successfully")
    @GetMapping("/recipient/{phone}")
    public ResponseEntity<DefaultApiResponse<Page<DeliveryResponse>>> getDeliveriesByRecipient(
            @Parameter(description = "Recipient Phone") @PathVariable String phone,
            @ParameterObject Pageable pageable) {
        Page<DeliveryResponse> response = deliveryService.getDeliveriesByRecipient(phone, pageable);
        DefaultApiResponse<Page<DeliveryResponse>> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Recipient deliveries retrieved successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }

    @Operation(summary = "Get deliveries by courier")
    @ApiResponse(responseCode = "200", description = "Courier deliveries retrieved successfully")
    @GetMapping("/courier/{courierId}")
    @PreAuthorize("hasAuthority('DELIVERY_READ_OWN') or hasAuthority('DELIVERY_READ')")
    public ResponseEntity<DefaultApiResponse<Page<DeliveryResponse>>> getDeliveriesByCourier(
            @Parameter(description = "Courier ID") @PathVariable Long courierId,
            @ParameterObject Pageable pageable) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Couriers can only see their own deliveries
        if (user.getRole().getName().name().equals("ROLE_COURIER") && !user.getId().equals(courierId)) {
            throw new org.springframework.security.access.AccessDeniedException("Access denied");
        }
        Page<DeliveryResponse> response = deliveryService.getDeliveriesByCourier(courierId, pageable);
        DefaultApiResponse<Page<DeliveryResponse>> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Courier deliveries retrieved successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }

    @Operation(summary = "Get my deliveries (for courier)")
    @ApiResponse(responseCode = "200", description = "My deliveries retrieved successfully")
    @GetMapping("/my-deliveries")
    @PreAuthorize("hasAuthority('DELIVERY_READ_OWN')")
    public ResponseEntity<DefaultApiResponse<Page<DeliveryResponse>>> getMyDeliveries(
            @ParameterObject Pageable pageable) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<DeliveryResponse> response = deliveryService.getMyDeliveries(user.getId(), pageable);
        DefaultApiResponse<Page<DeliveryResponse>> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "My deliveries retrieved successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }

    @Operation(summary = "Delete a delivery")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Delivery deleted successfully!"),
            @ApiResponse(responseCode = "400", description = "Couldn't delete a undelievered delivery!"),
            @ApiResponse(responseCode = "404", description = "Delivery not found")
    })
    @DeleteMapping("/{deliveryId}")
    @PreAuthorize("hasAuthority('DELIVERY_DELETE')")
    public ResponseEntity<DefaultApiResponse<Void>> deleteDelivery(
            @Parameter(description = "Delivery ID") @PathVariable Long deliveryId) {
        deliveryService.deleteDelivery(deliveryId);
        DefaultApiResponse<Void> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Delivery deleted successfully!",
                null,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }
}
