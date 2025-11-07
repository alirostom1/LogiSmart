package io.github.alirostom1.logismart.service;

import io.github.alirostom1.logismart.dto.request.zone.CreateZoneRequest;
import io.github.alirostom1.logismart.dto.response.zone.ZoneResponse;
import io.github.alirostom1.logismart.exception.PostalCodeAlreadyExistsException;
import io.github.alirostom1.logismart.exception.ResourceNotFoundException;
import io.github.alirostom1.logismart.mapper.ZoneMapper;
import io.github.alirostom1.logismart.model.entity.Zone;
import io.github.alirostom1.logismart.repository.ZoneRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ZoneService {
    private final ZoneRepo zoneRepo;
    private final ZoneMapper zoneMapper;

    // CREATE ZONE (FOR MANAGER)
    public ZoneResponse createZone(CreateZoneRequest request) {
        if (zoneRepo.existsByPostalCode(request.getPostalCode())) {
            throw new PostalCodeAlreadyExistsException(request.getPostalCode());
        }
        Zone zone = zoneMapper.toEntity(request);
        Zone savedZone = zoneRepo.save(zone);
        return zoneMapper.toResponse(savedZone);
    }

    // GET ZONE BY ID (FOR MANAGER)
    @Transactional(readOnly = true)
    public ZoneResponse getZoneById(String zoneId) {
        Zone zone = findById(zoneId);
        return zoneMapper.toResponse(zone);
    }

    // GET ALL ZONES (FOR MANAGER)
    @Transactional(readOnly = true)
    public Page<ZoneResponse> getAllZones(Pageable pageable) {
        Page<Zone> zones = zoneRepo.findAll(pageable);
        return zones.map(zoneMapper::toResponse);
    }

    // UPDATE ZONE (FOR MANAGER)
    public ZoneResponse updateZone(String zoneId, CreateZoneRequest request) {
        if(zoneRepo.existsByPostalCodeAndIdNot(request.getPostalCode(),UUID.fromString(zoneId))){
            throw new PostalCodeAlreadyExistsException(request.getPostalCode());
        }
        Zone zone = findById(zoneId);
        zone.setName(request.getName());
        zone.setPostalCode(request.getPostalCode());
        Zone updatedZone = zoneRepo.save(zone);
        return zoneMapper.toResponse(updatedZone);
    }

    // DELETE ZONE (FOR MANAGER)
    public void deleteZone(String zoneId) {
        if (!zoneRepo.existsById(UUID.fromString(zoneId))) {
            throw new ResourceNotFoundException("Zone", zoneId);
        }
        zoneRepo.deleteById(UUID.fromString(zoneId));
    }


    // UTIL METHOD
    private Zone findById(String zoneId) {
        return zoneRepo.findById(UUID.fromString(zoneId))
                .orElseThrow(() -> new ResourceNotFoundException("Zone", zoneId));
    }
}