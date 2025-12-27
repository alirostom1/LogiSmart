//package io.github.alirostom1.logismart.service;
//
//import io.github.alirostom1.logismart.dto.request.zone.CreateZoneRequest;
//import io.github.alirostom1.logismart.dto.response.zone.ZoneResponse;
//import io.github.alirostom1.logismart.exception.PostalCodeAlreadyExistsException;
//import io.github.alirostom1.logismart.exception.ResourceNotFoundException;
//import io.github.alirostom1.logismart.mapper.ZoneMapper;
//import io.github.alirostom1.logismart.model.entity.Zone;
//import io.github.alirostom1.logismart.repository.ZoneRepo;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mapstruct.factory.Mappers;
//import org.mockito.Mock;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(SpringExtension.class)
//public class ZoneServiceUnitTest {
//
//    @Mock
//    ZoneRepo zoneRepo;
//
//    ZoneMapper zoneMapper=Mappers.getMapper(ZoneMapper.class);;
//    ZoneService zoneService;
//
//    @BeforeEach
//    void setup() {
//        zoneService = new ZoneService(zoneRepo, zoneMapper);
//    }
//
//    //ZONES RETRIEVAL
//    @Test
//    public void getAllZones_should_return_list_zones() {
//        Zone zone1 = new Zone(UUID.randomUUID(), "zone1", 46000, List.of(), List.of());
//        Zone zone2 = new Zone(UUID.randomUUID(), "zone2", 32000, List.of(), List.of());
//        Pageable pageRequest = PageRequest.of(0, 20);
//        Page<Zone> page = new PageImpl<>(List.of(zone1, zone2), pageRequest, 2);
//        Page<ZoneResponse> responsePage = page.map(zoneMapper::toResponse);
//        when(zoneRepo.findAll(pageRequest)).thenReturn(page);
//        Page<ZoneResponse> zones = zoneService.getAllZones(PageRequest.of(0, 20));
//        assertEquals(zones,responsePage);
//    }
//
//    //ZONE RETRIEVAL
//    @Test
//    public void getZoneById_should_return_zone(){
//        UUID id = UUID.randomUUID();
//        Zone zone = new Zone(id,"zone1",59832,List.of(),List.of());
//        ZoneResponse zoneResponse = zoneMapper.toResponse(zone);
//        when(zoneRepo.findById(id)).thenReturn(Optional.of(zone));
//        ZoneResponse response = zoneService.getZoneById(id.toString());
//        assertEquals(zoneResponse,response);
//    }
//    @Test
//    public void getZoneById_should_throw_exception(){
//        UUID id = UUID.randomUUID();
//        when(zoneRepo.findById(id)).thenReturn(Optional.empty());
//        Exception exception = assertThrows(ResourceNotFoundException.class,() -> zoneService.getZoneById(id.toString()));
//        assertTrue(exception.getMessage().equals("Zone not found with id: " + id));
//    }
//
//
//    //ZONE CREATION
//    @Test
//    public void createZone_should_return_zone(){
//        UUID id = UUID.randomUUID();
//        CreateZoneRequest request = new CreateZoneRequest("zone1",59832);
//        Zone mockZone = zoneMapper.toEntity(request);
//        ZoneResponse mockZoneResponse = zoneMapper.toResponse(mockZone);
//        when(zoneRepo.save(any(Zone.class))).thenReturn(mockZone);
//        ZoneResponse zoneResponse = zoneService.createZone(request);
//        assertEquals(mockZoneResponse,zoneResponse);
//    }
//    @Test
//    public void createZone_should_throw_exception(){
//        UUID id = UUID.randomUUID();
//        CreateZoneRequest request = new CreateZoneRequest("zone1",59832);
//        when(zoneRepo.existsByPostalCode(59832)).thenReturn(true);
//        assertThrows(PostalCodeAlreadyExistsException.class,() -> zoneService.createZone(request));
//    }
//
//    //ZONE UPDATE
//    @Test
//    public void updateZone_should_return_zone(){
//        UUID id = UUID.randomUUID();
//        Zone mockZone = new Zone(id,"zone1",59832,null,null);
//        CreateZoneRequest request = new CreateZoneRequest("zone2",46000);
//        when(zoneRepo.existsByPostalCodeAndIdNot(request.getPostalCode(),mockZone.getId())).thenReturn(false);
//        when(zoneRepo.findById(id)).thenReturn(Optional.of(mockZone));
//        when(zoneRepo.save(any(Zone.class))).thenReturn(mockZone);
//        mockZone.setName(request.getName());
//        mockZone.setPostalCode(request.getPostalCode());
//        ZoneResponse mockZoneResponse = zoneMapper.toResponse(mockZone);
//        ZoneResponse zoneResponse = zoneService.updateZone(id.toString(),request);
//        assertEquals(mockZoneResponse,zoneResponse);
//    }
//    @Test
//    public void updateZone_should_throw_postal_code_duplication_exception(){
//        UUID id = UUID.randomUUID();
//        CreateZoneRequest request = new CreateZoneRequest("zone2",46000);
//        when(zoneRepo.existsByPostalCodeAndIdNot(request.getPostalCode(),id)).thenReturn(true);
//        assertThrows(PostalCodeAlreadyExistsException.class,() -> zoneService.updateZone(id.toString(),request));
//    }
//
//    @Test
//    public void updateZone_should_throw_not_found_exception(){
//        UUID id = UUID.randomUUID();
//        CreateZoneRequest request = new CreateZoneRequest("zone2",46000);
//        when(zoneRepo.existsByPostalCodeAndIdNot(any(Integer.class),any(UUID.class))).thenReturn(false);
//        when(zoneRepo.findById(id)).thenReturn(Optional.empty());
//        Exception exception = assertThrows(ResourceNotFoundException.class,() -> zoneService.updateZone(id.toString(),request));
//        assertTrue(exception.getMessage().contains("Zone not found"));
//    }
//    //ZONE DELETE
//    @Test
//    public void deleteZone_should_run(){
//        UUID id = UUID.randomUUID();
//        when(zoneRepo.existsById(id)).thenReturn(true);
//        zoneService.deleteZone(id.toString());
//        verify(zoneRepo,times(1)).deleteById(id);
//    }
//    @Test
//    public void deleteZone_should_throw_not_found_exception(){
//        UUID id = UUID.randomUUID();
//        when(zoneRepo.existsById(id)).thenReturn(false);
//        Exception exception = assertThrows(ResourceNotFoundException.class,() -> zoneService.deleteZone(id.toString()));
//        assertTrue(exception.getMessage().contains("Zone not found"));
//    }
//
//}
