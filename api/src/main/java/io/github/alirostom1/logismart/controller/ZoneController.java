package io.github.alirostom1.logismart.controller;

import io.github.alirostom1.logismart.dto.request.zone.AddPostalCodesToZoneRequest;
import io.github.alirostom1.logismart.dto.request.zone.CreateZoneRequest;
import io.github.alirostom1.logismart.dto.request.zone.CreateZoneWithPostalCodesRequest;
import io.github.alirostom1.logismart.dto.request.zone.PostalCodesRequest;
import io.github.alirostom1.logismart.dto.request.zone.RemovePostalCodesFromZoneRequest;
import io.github.alirostom1.logismart.dto.response.common.DefaultApiResponse;
import io.github.alirostom1.logismart.dto.response.zone.ZoneResponse;
import io.github.alirostom1.logismart.service.ZoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v3/zones")
@Tag(name = "Zone API", description = "Operations for zones")
@RequiredArgsConstructor
public class ZoneController {
    private final ZoneService zoneService;

    @Operation(summary = "Get all zones with pagination")
    @ApiResponse(responseCode = "200", description = "Zones retrieved successfully")
    @GetMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<DefaultApiResponse<Page<ZoneResponse>>> index(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page,size);
        Page<ZoneResponse> zonesPage = zoneService.getAllZones(pageable);
        DefaultApiResponse<Page<ZoneResponse>> response = new DefaultApiResponse<>(
                true,
                "Zones retrieved successfully",
                zonesPage,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get zone by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Zone retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Zone not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<DefaultApiResponse<ZoneResponse>> show(
            @Parameter(description = "Zone ID") @PathVariable("id") Long zoneId){
        ZoneResponse zoneResponse = zoneService.getZoneById(zoneId);
        DefaultApiResponse<ZoneResponse> response = new DefaultApiResponse<>(
                true,
                "Zone retrieved successfully!",
                zoneResponse,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create zone")
    @ApiResponse(responseCode = "201", description = "Zone created successfully")
    @PostMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<DefaultApiResponse<ZoneResponse>> store(@Valid @RequestBody CreateZoneRequest request){
        ZoneResponse zoneResponse = zoneService.createZone(request);
        DefaultApiResponse<ZoneResponse> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Zone Created Successfully",
                zoneResponse,
                System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(defaultApiResponse);
    }

    @Operation(summary = "Update zone")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Zone updated successfully"),
            @ApiResponse(responseCode = "404", description = "Zone not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<DefaultApiResponse<ZoneResponse>> update(
            @Parameter(description = "Zone ID") @PathVariable("id") Long id,
            @Valid @RequestBody CreateZoneRequest request){
        ZoneResponse zoneResponse = zoneService.updateZone(id,request);
        DefaultApiResponse<ZoneResponse> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Zone updated Successfully!",
                zoneResponse,
                System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(defaultApiResponse);
    }

    @Operation(summary = "Delete zone")
    @ApiResponse(responseCode = "200", description = "Zone deleted successfully")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<DefaultApiResponse<Void>> delete(
            @Parameter(description = "Zone ID") @PathVariable("id") Long id){
        zoneService.deleteZone(id);
        DefaultApiResponse<Void> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Zone deleted Successfully!",
                null,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }

    @Operation(summary = "Create zone with postal codes")
    @ApiResponse(responseCode = "201", description = "Zone created successfully with postal codes")
    @PostMapping("/with-postal-codes")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<DefaultApiResponse<ZoneResponse>> createZoneWithPostalCodes(
            @Valid @RequestBody CreateZoneWithPostalCodesRequest request){
        ZoneResponse zoneResponse = zoneService.createZoneWithPostalCodes(request);
        DefaultApiResponse<ZoneResponse> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Zone Created Successfully with Postal Codes",
                zoneResponse,
                System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(defaultApiResponse);
    }

    @Operation(summary = "Add postal codes to zone")
    @ApiResponse(responseCode = "200", description = "Postal codes added successfully")
    @PostMapping("/{id}/postal-codes")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<DefaultApiResponse<Void>> addPostalCodesToZone(
            @Parameter(description = "Zone ID") @PathVariable("id") Long zoneId,
            @Valid @RequestBody PostalCodesRequest request){
        AddPostalCodesToZoneRequest requestWithId = new AddPostalCodesToZoneRequest(zoneId, request.postalCodes());
        zoneService.addPostalCodesToZone(requestWithId);
        DefaultApiResponse<Void> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Postal codes added successfully!",
                null,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }

    @Operation(summary = "Remove postal codes from zone")
    @ApiResponse(responseCode = "200", description = "Postal codes removed successfully")
    @DeleteMapping("/{id}/postal-codes")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<DefaultApiResponse<Void>> removePostalCodesFromZone(
            @Parameter(description = "Zone ID") @PathVariable("id") Long zoneId,
            @Valid @RequestBody PostalCodesRequest request){
        RemovePostalCodesFromZoneRequest requestWithId = new RemovePostalCodesFromZoneRequest(zoneId, request.postalCodes());
        zoneService.removePostalCodesFromZone(requestWithId);
        DefaultApiResponse<Void> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Postal codes removed successfully!",
                null,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }

}
