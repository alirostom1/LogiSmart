package io.github.alirostom1.logismart.controller;

import io.github.alirostom1.logismart.dto.request.zone.CreateZoneRequest;
import io.github.alirostom1.logismart.dto.response.common.ApiResponse;
import io.github.alirostom1.logismart.dto.response.zone.ZoneResponse;
import io.github.alirostom1.logismart.service.ZoneService;
import io.github.alirostom1.logismart.util.ValidUUID;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/v2/zone")
@RequiredArgsConstructor
public class ZoneController {
    private final ZoneService zoneService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ZoneResponse>>> index(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size){
        Pageable pageable = PageRequest.of(page,size);
        Page<ZoneResponse> zonesPage = zoneService.getAllZones(pageable);
        ApiResponse<Page<ZoneResponse>> response = new ApiResponse<>(
                true,
                "Zones retrieved successfully",
                zonesPage,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ZoneResponse>> show(@PathVariable("id") @ValidUUID String zoneId){
        ZoneResponse zoneResponse = zoneService.getZoneById(zoneId);
        ApiResponse<ZoneResponse> response = new ApiResponse<>(
                true,
                "Zone retrieved successfully!",
                zoneResponse,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(response);
    }
    @PostMapping
    public ResponseEntity<ApiResponse<ZoneResponse>> store(@Valid @RequestBody CreateZoneRequest request){
        ZoneResponse zoneResponse = zoneService.createZone(request);
        ApiResponse<ZoneResponse> apiResponse = new ApiResponse<>(
                true,
                "Zone Created Successfully",
                zoneResponse,
                System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ZoneResponse>> update(
            @PathVariable("id") @ValidUUID String id,
            @Valid @RequestBody CreateZoneRequest request){
        ZoneResponse zoneResponse = zoneService.updateZone(id,request);
        ApiResponse<ZoneResponse> apiResponse = new ApiResponse<>(
                true,
                "Zone updated Successfully!",
                zoneResponse,
                System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<ZoneResponse>> delete(@PathVariable("id") @ValidUUID String id){
        zoneService.deleteZone(id);
        ApiResponse<ZoneResponse> apiResponse = new ApiResponse<>(
                true,
                "Zone deleted Successfully!",
                null,
                System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

}
