package io.github.alirostom1.logismart.service;


import io.github.alirostom1.logismart.dto.request.zone.AddPostalCodesToZoneRequest;
import io.github.alirostom1.logismart.dto.request.zone.CreateZoneRequest;
import io.github.alirostom1.logismart.dto.request.zone.CreateZoneWithPostalCodesRequest;
import io.github.alirostom1.logismart.dto.response.zone.ZoneResponse;
import io.github.alirostom1.logismart.exception.ResourceNotFoundException;
import io.github.alirostom1.logismart.exception.ZoneNotServicedException;
import io.github.alirostom1.logismart.mapper.ZoneMapper;
import io.github.alirostom1.logismart.model.entity.Zone;
import io.github.alirostom1.logismart.model.entity.ZonePostalCode;
import io.github.alirostom1.logismart.repository.ZonePostalCodeRepo;
import io.github.alirostom1.logismart.repository.ZoneRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class ZoneService {
    private final ZonePostalCodeRepo zonePostalCodeRepo;
    private final ZoneMapper zoneMapper;
    private final ZoneRepo zoneRepo;

    @Transactional(readOnly = true)
    public ZoneResponse getZoneByPostalCode(String postalCode) {
        Zone zone = zonePostalCodeRepo.findZoneByPostalCode(postalCode.strip())
                .orElseThrow(() -> new ZoneNotServicedException(
                        "Sorry, we don't deliver to postal code: " + postalCode
                ));
        return zoneMapper.toResponse(zone);
    }

    public void validateDeliveryZones(Zone pickupZone, Zone deliveryZone) {
        if (!pickupZone.isActive()) {
            throw new ZoneNotServicedException("Pickup zone is currently unavailable");
        }
        if (!deliveryZone.isActive()) {
            throw new ZoneNotServicedException("Delivery zone is currently unavailable");
        }
    }

    public ZoneResponse createZone(CreateZoneRequest request) {
        Zone zone = Zone.builder()
                .name(request.name())
                .code(request.code())
                .active(true)
                .build();
        Zone savedZone = zoneRepo.save(zone);
        return zoneMapper.toResponse(zone);
    }

    public void addPostalCodesToZone(AddPostalCodesToZoneRequest request) {
        Zone zone = zoneRepo.findById(request.zoneId())
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));

        request.postalCodes().forEach(pc -> {
            ZonePostalCode zpc = new ZonePostalCode();
            zpc.setZone(zone);
            zpc.setPostalCode(pc.strip().toUpperCase());
            zonePostalCodeRepo.save(zpc);
        });
    }

    public ZoneResponse createZoneWithPostalCodes(CreateZoneWithPostalCodesRequest request) {
        Zone zone = Zone.builder()
                .name(request.name())
                .code(request.code())
                .active(true)
                .build();

        Zone savedZone = zone;

        List<ZonePostalCode> zonePostalCodes = request.postalCodes().stream()
                .map(pc -> {
                    ZonePostalCode zpc = new ZonePostalCode();
                    zpc.setZone(zone);
                    zpc.setPostalCode(pc.strip().toUpperCase());
                    return zpc;
                })
                .toList();

        zonePostalCodeRepo.saveAll(zonePostalCodes);
        return zoneMapper.toResponse(zone);
    }

    @Transactional(readOnly = true)
    public Page<ZoneResponse> getAllZones(Pageable pageable) {
        return zoneRepo.findAll(pageable).map(zoneMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ZoneResponse getZoneById(Long zoneId) {
        Zone zone = zoneRepo.findById(zoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));
        return zoneMapper.toResponse(zone);
    }

    public ZoneResponse updateZone(Long zoneId, CreateZoneRequest request) {
        Zone zone = zoneRepo.findById(zoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));
        zone.setName(request.name());
        zone.setCode(request.code());
        Zone updated = zoneRepo.save(zone);
        return zoneMapper.toResponse(updated);
    }

    public void deleteZone(Long zoneId) {
        if (!zoneRepo.existsById(zoneId)) {
            throw new ResourceNotFoundException("Zone not found");
        }
        zoneRepo.deleteById(zoneId);
    }

}