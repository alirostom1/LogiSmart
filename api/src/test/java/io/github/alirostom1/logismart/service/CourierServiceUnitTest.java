package io.github.alirostom1.logismart.service;

import io.github.alirostom1.logismart.dto.request.courier.CreateCourierRequest;
import io.github.alirostom1.logismart.dto.request.courier.UpdateCourierRequest;
import io.github.alirostom1.logismart.dto.response.courier.CourierResponse;
import io.github.alirostom1.logismart.dto.response.courier.CourierWithDeliveriesResponse;
import io.github.alirostom1.logismart.exception.EmailAlreadyExistsException;
import io.github.alirostom1.logismart.exception.PhoneAlreadyExistsException;
import io.github.alirostom1.logismart.exception.ResourceNotFoundException;
import io.github.alirostom1.logismart.mapper.CourierMapper;
import io.github.alirostom1.logismart.model.entity.Courier;
import io.github.alirostom1.logismart.model.entity.Role;
import io.github.alirostom1.logismart.model.entity.Zone;
import io.github.alirostom1.logismart.model.enums.ERole;
import io.github.alirostom1.logismart.repository.CourierRepo;
import io.github.alirostom1.logismart.repository.RoleRepository;
import io.github.alirostom1.logismart.repository.ZoneRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CourierService Unit Tests")
class CourierServiceUnitTest{

    @Mock
    private CourierRepo courierRepo;

    @Mock
    private CourierMapper courierMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ZoneRepo zoneRepo;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private CourierService courierService;

    private Courier courier;
    private Zone zone;
    private Role courierRole;
    private CreateCourierRequest createRequest;
    private UpdateCourierRequest updateRequest;
    private CourierResponse courierResponse;

    @BeforeEach
    void setUp() {
        // Setup Zone
        zone = new Zone();
        zone.setId(1L);
        zone.setName("Zone A");

        // Setup Role
        courierRole = new Role();
        courierRole.setId(1L);
        courierRole.setName(ERole.ROLE_COURIER);

        // Setup Courier
        courier = new Courier();
        courier.setId(1L);
        courier.setFirstName("John");
        courier.setLastName("Doe");
        courier.setEmail("john.doe@example.com");
        courier.setPhone("0612345678");
        courier.setPassword("encodedPassword");
        courier.setZone(zone);
        courier.setRole(courierRole);

        // Setup CreateCourierRequest
        createRequest = new CreateCourierRequest();
        createRequest.setFirstName("John");
        createRequest.setLastName("Doe");
        createRequest.setEmail("john.doe@example.com");
        createRequest.setPhone("0612345678");
        createRequest.setPassword("password123");
        createRequest.setZoneId(1L);

        // Setup UpdateCourierRequest
        updateRequest = new UpdateCourierRequest();
        updateRequest.setFirstName("John");
        updateRequest.setLastName("Doe Updated");
        updateRequest.setEmail("john.updated@example.com");
        updateRequest.setPhoneNumber("0698765432");
        updateRequest.setZoneId(1L);

        // Setup CourierResponse
        courierResponse = new CourierResponse();
        courierResponse.setId(1L);
        courierResponse.setFirstName("John");
        courierResponse.setLastName("Doe");
        courierResponse.setEmail("john.doe@example.com");
    }

    // ==================== CREATE COURIER TESTS ====================

    @Nested
    @DisplayName("createCourier()")
    class CreateCourierTests {

        @Test
        @DisplayName("Should create courier successfully")
        void shouldCreateCourierSuccessfully() {
            // Given
            when(zoneRepo.findById(1L)).thenReturn(Optional.of(zone));
            when(courierRepo.existsByPhone(anyString())).thenReturn(false);
            when(courierRepo.existsByEmail(anyString())).thenReturn(false);
            when(courierMapper.toEntity(any(CreateCourierRequest.class))).thenReturn(courier);
            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
            when(roleRepository.findByName(ERole.ROLE_COURIER)).thenReturn(Optional.of(courierRole));
            when(courierRepo.save(any(Courier.class))).thenReturn(courier);
            when(courierMapper.toResponse(any(Courier.class))).thenReturn(courierResponse);

            // When
            CourierResponse result = courierService.createCourier(createRequest);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getFirstName()).isEqualTo("John");

            verify(zoneRepo).findById(1L);
            verify(courierRepo).existsByPhone(createRequest.getPhone());
            verify(courierRepo).existsByEmail(createRequest.getEmail());
            verify(passwordEncoder).encode(createRequest.getPassword());
            verify(roleRepository).findByName(ERole.ROLE_COURIER);
            verify(courierRepo).save(any(Courier.class));
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when zone not found")
        void shouldThrowExceptionWhenZoneNotFound() {
            // Given
            when(zoneRepo.findById(1L)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> courierService.createCourier(createRequest))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Zone not found");

            verify(courierRepo, never()).save(any());
        }

        @Test
        @DisplayName("Should throw PhoneAlreadyExistsException when phone exists")
        void shouldThrowExceptionWhenPhoneExists() {
            // Given
            when(zoneRepo.findById(1L)).thenReturn(Optional.of(zone));
            when(courierRepo.existsByPhone(createRequest.getPhone())).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> courierService.createCourier(createRequest))
                    .isInstanceOf(PhoneAlreadyExistsException.class);

            verify(courierRepo, never()).save(any());
        }

        @Test
        @DisplayName("Should throw EmailAlreadyExistsException when email exists")
        void shouldThrowExceptionWhenEmailExists() {
            // Given
            when(zoneRepo.findById(1L)).thenReturn(Optional.of(zone));
            when(courierRepo.existsByPhone(anyString())).thenReturn(false);
            when(courierRepo.existsByEmail(createRequest.getEmail())).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> courierService.createCourier(createRequest))
                    .isInstanceOf(EmailAlreadyExistsException.class);

            verify(courierRepo, never()).save(any());
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when courier role not found")
        void shouldThrowExceptionWhenCourierRoleNotFound() {
            // Given
            when(zoneRepo.findById(1L)).thenReturn(Optional.of(zone));
            when(courierRepo.existsByPhone(anyString())).thenReturn(false);
            when(courierRepo.existsByEmail(anyString())).thenReturn(false);
            when(courierMapper.toEntity(any(CreateCourierRequest.class))).thenReturn(courier);
            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
            when(roleRepository.findByName(ERole.ROLE_COURIER)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> courierService.createCourier(createRequest))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Courier Role not found!");

            verify(courierRepo, never()).save(any());
        }
    }

    // ==================== GET COURIER BY ID TESTS ====================

    @Nested
    @DisplayName("getCourierById()")
    class GetCourierByIdTests {

        @Test
        @DisplayName("Should return courier when found")
        void shouldReturnCourierWhenFound() {
            // Given
            when(courierRepo.findById(1L)).thenReturn(Optional.of(courier));
            when(courierMapper.toResponse(courier)).thenReturn(courierResponse);

            // When
            CourierResponse result = courierService.getCourierById(1L);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            verify(courierRepo).findById(1L);
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when courier not found")
        void shouldThrowExceptionWhenCourierNotFound() {
            // Given
            when(courierRepo.findById(1L)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> courierService.getCourierById(1L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Courier not found");
        }
    }

    // ==================== GET COURIER WITH DELIVERIES TESTS ====================

    @Nested
    @DisplayName("getCourierWithDeliveries()")
    class GetCourierWithDeliveriesTests {

        @Test
        @DisplayName("Should return courier with deliveries when found")
        void shouldReturnCourierWithDeliveriesWhenFound() {
            // Given
            CourierWithDeliveriesResponse responseWithDeliveries = new CourierWithDeliveriesResponse();
            responseWithDeliveries.setId(1L);

            when(courierRepo.findById(1L)).thenReturn(Optional.of(courier));
            when(courierMapper.toWithDeliveriesResponse(courier)).thenReturn(responseWithDeliveries);

            // When
            CourierWithDeliveriesResponse result = courierService.getCourierWithDeliveries(1L);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            verify(courierMapper).toWithDeliveriesResponse(courier);
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when courier not found")
        void shouldThrowExceptionWhenCourierNotFound() {
            // Given
            when(courierRepo.findById(1L)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> courierService.getCourierWithDeliveries(1L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Courier not found");
        }
    }

    // ==================== UPDATE COURIER TESTS ====================

    @Nested
    @DisplayName("updateCourier()")
    class UpdateCourierTests {

        @Test
        @DisplayName("Should update courier successfully")
        void shouldUpdateCourierSuccessfully() {
            // Given
            when(courierRepo.findById(1L)).thenReturn(Optional.of(courier));
            when(zoneRepo.findById(1L)).thenReturn(Optional.of(zone));
            when(courierRepo.existsByPhoneAndIdNot(anyString(), anyLong())).thenReturn(false);
            when(courierRepo.existsByEmailAndIdNot(anyString(), anyLong())).thenReturn(false);
            when(courierRepo.save(any(Courier.class))).thenReturn(courier);
            when(courierMapper.toResponse(any(Courier.class))).thenReturn(courierResponse);

            // When
            CourierResponse result = courierService.updateCourier(1L, updateRequest);

            // Then
            assertThat(result).isNotNull();
            verify(courierMapper).updateFromRequest(updateRequest, courier);
            verify(courierRepo).save(courier);
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when courier not found")
        void shouldThrowExceptionWhenCourierNotFound() {
            // Given
            when(courierRepo.findById(1L)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> courierService.updateCourier(1L, updateRequest))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Courier not found");

            verify(courierRepo, never()).save(any());
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when zone not found")
        void shouldThrowExceptionWhenZoneNotFound() {
            // Given
            when(courierRepo.findById(1L)).thenReturn(Optional.of(courier));
            when(zoneRepo.findById(1L)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> courierService.updateCourier(1L, updateRequest))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Zone not found");

            verify(courierRepo, never()).save(any());
        }

        @Test
        @DisplayName("Should throw PhoneAlreadyExistsException when phone exists for another courier")
        void shouldThrowExceptionWhenPhoneExistsForAnotherCourier() {
            // Given
            when(courierRepo.findById(1L)).thenReturn(Optional.of(courier));
            when(zoneRepo.findById(1L)).thenReturn(Optional.of(zone));
            when(courierRepo.existsByPhoneAndIdNot(updateRequest.getPhoneNumber(), 1L)).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> courierService.updateCourier(1L, updateRequest))
                    .isInstanceOf(PhoneAlreadyExistsException.class);

            verify(courierRepo, never()).save(any());
        }

        @Test
        @DisplayName("Should throw EmailAlreadyExistsException when email exists for another courier")
        void shouldThrowExceptionWhenEmailExistsForAnotherCourier() {
            // Given
            when(courierRepo.findById(1L)).thenReturn(Optional.of(courier));
            when(zoneRepo.findById(1L)).thenReturn(Optional.of(zone));
            when(courierRepo.existsByPhoneAndIdNot(anyString(), anyLong())).thenReturn(false);
            when(courierRepo.existsByEmailAndIdNot(updateRequest.getEmail(), 1L)).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> courierService.updateCourier(1L, updateRequest))
                    .isInstanceOf(EmailAlreadyExistsException.class);

            verify(courierRepo, never()).save(any());
        }
    }

    // ==================== GET ALL COURIERS TESTS ====================

    @Nested
    @DisplayName("getAllCouriers()")
    class GetAllCouriersTests {

        @Test
        @DisplayName("Should return paginated couriers")
        void shouldReturnPaginatedCouriers() {
            // Given
            Pageable pageable = PageRequest.of(0, 10);
            List<Courier> courierList = List.of(courier);
            Page<Courier> courierPage = new PageImpl<>(courierList, pageable, 1);

            when(courierRepo.findAll(pageable)).thenReturn(courierPage);
            when(courierMapper.toResponse(any(Courier.class))).thenReturn(courierResponse);

            // When
            Page<CourierResponse> result = courierService.getAllCouriers(pageable);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getTotalElements()).isEqualTo(1);
            verify(courierRepo).findAll(pageable);
        }

        @Test
        @DisplayName("Should return empty page when no couriers exist")
        void shouldReturnEmptyPageWhenNoCouriersExist() {
            // Given
            Pageable pageable = PageRequest.of(0, 10);
            Page<Courier> emptyPage = new PageImpl<>(List.of(), pageable, 0);

            when(courierRepo.findAll(pageable)).thenReturn(emptyPage);

            // When
            Page<CourierResponse> result = courierService.getAllCouriers(pageable);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
            assertThat(result.getTotalElements()).isZero();
        }
    }

    // ==================== GET COURIERS BY ZONE TESTS ====================

    @Nested
    @DisplayName("getCouriersByZone()")
    class GetCouriersByZoneTests {

        @Test
        @DisplayName("Should return couriers by zone")
        void shouldReturnCouriersByZone() {
            // Given
            Pageable pageable = PageRequest.of(0, 10);
            List<Courier> courierList = List.of(courier);
            Page<Courier> courierPage = new PageImpl<>(courierList, pageable, 1);

            when(zoneRepo.findById(1L)).thenReturn(Optional.of(zone));
            when(courierRepo.findCouriersByZone(zone, pageable)).thenReturn(courierPage);
            when(courierMapper.toResponse(any(Courier.class))).thenReturn(courierResponse);

            // When
            Page<CourierResponse> result = courierService.getCouriersByZone(1L, pageable);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            verify(courierRepo).findCouriersByZone(zone, pageable);
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when zone not found")
        void shouldThrowExceptionWhenZoneNotFound() {
            // Given
            Pageable pageable = PageRequest.of(0, 10);
            when(zoneRepo.findById(1L)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> courierService.getCouriersByZone(1L, pageable))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Zone not found!");

            verify(courierRepo, never()).findCouriersByZone(any(), any());
        }
    }

    // ==================== DELETE COURIER TESTS ====================

    @Nested
    @DisplayName("deleteCourier()")
    class DeleteCourierTests {

        @Test
        @DisplayName("Should delete courier successfully")
        void shouldDeleteCourierSuccessfully() {
            // Given
            when(courierRepo.existsById(1L)).thenReturn(true);
            doNothing().when(courierRepo).deleteById(1L);

            // When
            courierService.deleteCourier(1L);

            // Then
            verify(courierRepo).existsById(1L);
            verify(courierRepo).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when courier not found")
        void shouldThrowExceptionWhenCourierNotFound() {
            // Given
            when(courierRepo.existsById(1L)).thenReturn(false);

            // When & Then
            assertThatThrownBy(() -> courierService.deleteCourier(1L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Courier not found");

            verify(courierRepo, never()).deleteById(anyLong());
        }
    }
}