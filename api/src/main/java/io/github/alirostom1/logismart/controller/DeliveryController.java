package io.github.alirostom1.logismart.controller;

import io.github.alirostom1.logismart.dto.ApiResponse;
import io.github.alirostom1.logismart.dto.ApiResponseSuccess;
import io.github.alirostom1.logismart.dto.deliverydto.CreateDeliveryDto;
import io.github.alirostom1.logismart.dto.deliverydto.UpdateDeliveryDto;
import io.github.alirostom1.logismart.model.entity.Delivery;
import io.github.alirostom1.logismart.model.enums.DeliveryStatus;
import io.github.alirostom1.logismart.service.DeliveryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {
    @Autowired
    private DeliveryService deliveryService;


    @GetMapping
    public ResponseEntity<ApiResponse> index(){
        List<Delivery> deliveries = deliveryService.getAllDeliveries();
        ApiResponseSuccess<List<Delivery>> response = new ApiResponseSuccess<>(
                200,
                "Deliveries retrieved successfully!",
                deliveries
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> show(@PathVariable("id") UUID id){
        Delivery delivery = deliveryService.getDeliveryById(id);
        ApiResponseSuccess<Delivery> response = new ApiResponseSuccess<>(
                200,
                "Delivery retrieved successfully!",
                delivery
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping
    public ResponseEntity<ApiResponse> store(@Valid @RequestBody CreateDeliveryDto dto){
        Delivery delivery = deliveryService.createDelivery(dto);
        ApiResponseSuccess<Delivery> response = new ApiResponseSuccess<>(
                201,
                "Delivery created successfully!",
                delivery
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable("id") UUID id,@Valid @RequestBody UpdateDeliveryDto dto){
        dto.setId(id);
        Delivery delivery = deliveryService.updateDelivery(dto);
        ApiResponseSuccess<Delivery> response = new ApiResponseSuccess<>(
                201,
                "Delivery updated successfully!",
                delivery
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") UUID id){
        deliveryService.deleteDelivery(id);
        ApiResponse response = new ApiResponse(
                200,
                "Delivery deleted successfully!"
        );
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<ApiResponse> updateStatus(@PathVariable("id") UUID id,@PathVariable("status") DeliveryStatus status){
        Delivery delivery = deliveryService.updateStatus(id,status);
        ApiResponseSuccess<Delivery> response = new ApiResponseSuccess<>(
                201,
                "Delivery status updated successfully!",
                delivery
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/courier/{id}")
    public ResponseEntity<ApiResponse> getDeliveriesByCourier(@PathVariable("id") UUID id){
        List<Delivery> deliveries = deliveryService.getDeliveriesByCourierId(id);
        ApiResponseSuccess<List<Delivery>> response = new ApiResponseSuccess<>(
                200,
                !deliveries.isEmpty() ? "Deliveries with courier id: "+ id + " retrieved successfully!" : "Courier with id: " + id + " has no deliveries!",
                deliveries
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
