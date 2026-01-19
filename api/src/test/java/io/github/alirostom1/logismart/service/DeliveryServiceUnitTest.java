//package io.github.alirostom1.logismart.service;
//
//import io.github.alirostom1.logismart.dto.request.delivery.AssignDeliveryRequest;
//import io.github.alirostom1.logismart.dto.request.delivery.CreateDeliveryRequest;
//import io.github.alirostom1.logismart.dto.request.delivery.SearchDeliveryRequest;
//import io.github.alirostom1.logismart.dto.request.delivery.UpdateDeliveryStatusRequest;
//import io.github.alirostom1.logismart.dto.response.delivery.DeliveryDetailsResponse;
//import io.github.alirostom1.logismart.dto.response.delivery.DeliveryResponse;
//import io.github.alirostom1.logismart.dto.response.delivery.DeliveryTrackingResponse;
//import io.github.alirostom1.logismart.exception.DeliveryCourierZoneMismatchException;
//import io.github.alirostom1.logismart.exception.ResourceNotFoundException;
//import io.github.alirostom1.logismart.exception.ZoneNotServicedException;
//import io.github.alirostom1.logismart.mapper.DeliveryMapper;
//import io.github.alirostom1.logismart.model.entity.*;
//import io.github.alirostom1.logismart.model.enums.DeliveryStatus;
//import io.github.alirostom1.logismart.repository.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.security.access.AccessDeniedException;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("DeliveryService Unit Tests")
//class DeliveryServiceUnitTest {
//
//    @Mock
//    private DeliveryRepo deliveryRepo;
//
//    @Mock
//    private DeliveryMapper deliveryMapper;
//
//    @Mock
//    private SenderRepo senderRepo;
//
//    @Mock
//    private ZonePostalCodeRepo zonePostalCodeRepo;
//
//    @Mock
//    private ZoneService zoneService;
//
//    @Mock
//    private DeliveryHistoryRepo deliveryHistoryRepo;
//
//    @Mock
//    private ProductRepo productRepo;
//
//    @Mock
//    private DeliveryProductRepo deliveryProductRepo;
//
//    @Mock
//    private RecipientService recipientService;
//
//    @Mock
//    private CourierRepo courierRepo;
//
//    @InjectMocks
//    private DeliveryService deliveryService;
//
//    // Test entities
//    private Delivery delivery;
//    private Sender sender;
//    private Recipient recipient;
//    private Zone pickupZone;
//    private Zone shippingZone;
//    private Courier collectingCourier;
//    private Courier shippingCourier;
//    private Product product;
//    private DeliveryHistory deliveryHistory;
//
//    // DTOs
//    private CreateDeliveryRequest createRequest;
//    private UpdateDeliveryStatusRequest updateStatusRequest;
//    private AssignDeliveryRequest assignRequest;
//    private SearchDeliveryRequest searchRequest;
//    private DeliveryDetailsResponse detailsResponse;
//    private DeliveryResponse deliveryResponse;
//    private DeliveryTrackingResponse trackingResponse;
//
//    @BeforeEach
//    void setUp() {
//        // Setup Zones
//        pickupZone = new Zone();
//        pickupZone.setId(1L);
//        pickupZone.setName("Pickup Zone");
//        pickupZone.setActive(true);
//
//        shippingZone = new Zone();
//        shippingZone.setId(2L);
//        shippingZone.setName("Shipping Zone");
//        shippingZone.setActive(true);
//
//        // Setup Sender
//        sender = new Sender();
//        sender.setId(1L);
//        sender.setFirstName("Sender");
//        sender.setLastName("Test");
//        sender.setEmail("sender@test.com");
//
//        // Setup Recipient
//        recipient = new Recipient();
//        recipient.setId(1L);
//        recipient.setName("Recipient1");
//        recipient.setPhone("0612345678");
//
//        // Setup Couriers
//        collectingCourier = new Courier();
//        collectingCourier.setId(1L);
//        collectingCourier.setFirstName("Collecting");
//        collectingCourier.setLastName("Courier");
//        collectingCourier.setZone(pickupZone);
//
//        shippingCourier = new Courier();
//        shippingCourier.setId(2L);
//        shippingCourier.setFirstName("Shipping");
//        shippingCourier.setLastName("Courier");
//        shippingCourier.setZone(shippingZone);
//
//        // Setup Product
//        product = new Product();
//        product.setId(1L);
//        product.setName("Test Product");
//        product.setUnitPrice(BigDecimal.valueOf(100));
//
//        // Setup Delivery
//        delivery = new Delivery();
//        delivery.setId(1L);
//        delivery.setTrackingNumber("TRACK123456");
//        delivery.setStatus(DeliveryStatus.CREATED);
//        delivery.setSender(sender);
//        delivery.setRecipient(recipient);
//        delivery.setPickupZone(pickupZone);
//        delivery.setShippingZone(shippingZone);
//        delivery.setDeliveryHistoryList(new ArrayList<>());
//        delivery.setDeliveryProducts(new ArrayList<>());
//
//        // Setup DeliveryHistory
//        deliveryHistory = new DeliveryHistory();
//        deliveryHistory.setId(1L);
//        deliveryHistory.setDelivery(delivery);
//        deliveryHistory.setStatus(DeliveryStatus.CREATED);
//        deliveryHistory.setComment("Delivery created");
//
//        // Setup CreateDeliveryRequest
//        createRequest = new CreateDeliveryRequest();
//        createRequest.setPickupPostalCode("10000");
//        createRequest.setShippingPostalCode("20000");
//        createRequest.setPickupAddress("123 Pickup Street");
//        createRequest.setShippingAddress("456 Shipping Street");
//
//        // Setup UpdateDeliveryStatusRequest
//        updateStatusRequest = new UpdateDeliveryStatusRequest();
//        updateStatusRequest.setStatus("IN_TRANSIT");
//        updateStatusRequest.setComment("Package picked up");
//
//        // Setup AssignDeliveryRequest
//        assignRequest = new AssignDeliveryRequest();
//        assignRequest.setCourierId(1L);
//
//        // Setup SearchDeliveryRequest
//        searchRequest = new SearchDeliveryRequest();
//
//        // Setup Response DTOs
//        detailsResponse = new DeliveryDetailsResponse();
//        detailsResponse.setId(1L);
//        detailsResponse.setTrackingNumber("TRACK123456");
//
//        deliveryResponse = new DeliveryResponse();
//        deliveryResponse.setId(1L);
//        deliveryResponse.setTrackingNumber("TRACK123456");
//
//        trackingResponse = new DeliveryTrackingResponse();
//        trackingResponse.setTrackingNumber("TRACK123456");
//    }
//
//    // ==================== CREATE DELIVERY TESTS ====================
//
//    @Nested
//    @DisplayName("createDelivery()")
//    class CreateDeliveryTests {
//
//        @Test
//        @DisplayName("Should create delivery successfully without products")
//        void shouldCreateDeliverySuccessfullyWithoutProducts() {
//            // Given
//            when(senderRepo.findById(1L)).thenReturn(Optional.of(sender));
//            when(recipientService.findOrCreateRecipient(any())).thenReturn(recipient);
//            when(zonePostalCodeRepo.findZoneByPostalCode("10000")).thenReturn(Optional.of(pickupZone));
//            when(zonePostalCodeRepo.findZoneByPostalCode("20000")).thenReturn(Optional.of(shippingZone));
//            doNothing().when(zoneService).validateDeliveryZones(pickupZone, shippingZone);
//            when(deliveryMapper.toEntity(any(CreateDeliveryRequest.class))).thenReturn(delivery);
//            when(deliveryRepo.save(any(Delivery.class))).thenReturn(delivery);
//            when(deliveryHistoryRepo.save(any(DeliveryHistory.class))).thenReturn(deliveryHistory);
//            when(deliveryMapper.toDetailsResponse(any(Delivery.class))).thenReturn(detailsResponse);
//
//            // When
//            DeliveryDetailsResponse result = deliveryService.createDelivery(createRequest, 1L);
//
//            // Then
//            assertThat(result).isNotNull();
//            assertThat(result.getTrackingNumber()).isEqualTo("TRACK123456");
//
//            verify(senderRepo).findById(1L);
//            verify(zoneService).validateDeliveryZones(pickupZone, shippingZone);
//            verify(deliveryRepo).save(any(Delivery.class));
//            verify(deliveryHistoryRepo).save(any(DeliveryHistory.class));
//        }
//
//        @Test
//        @DisplayName("Should create delivery successfully with products")
//        void shouldCreateDeliverySuccessfullyWithProducts() {
//            // Given
//            CreateDeliveryRequest.DeliveryProductRequest productRequest = new CreateDeliveryRequest.DeliveryProductRequest();
//            productRequest.setProductId(1L);
//            productRequest.setQuantity(2);
//            createRequest.setProducts(List.of(productRequest));
//
//            DeliveryProduct deliveryProduct = new DeliveryProduct();
//            deliveryProduct.setProduct(product);
//            deliveryProduct.setQuantity(2);
//            deliveryProduct.setPriceAtOrder(BigDecimal.valueOf(200));
//
//            when(senderRepo.findById(1L)).thenReturn(Optional.of(sender));
//            when(recipientService.findOrCreateRecipient(any())).thenReturn(recipient);
//            when(zonePostalCodeRepo.findZoneByPostalCode("10000")).thenReturn(Optional.of(pickupZone));
//            when(zonePostalCodeRepo.findZoneByPostalCode("20000")).thenReturn(Optional.of(shippingZone));
//            doNothing().when(zoneService).validateDeliveryZones(pickupZone, shippingZone);
//            when(deliveryMapper.toEntity(any(CreateDeliveryRequest.class))).thenReturn(delivery);
//            when(deliveryRepo.save(any(Delivery.class))).thenReturn(delivery);
//            when(productRepo.findById(1L)).thenReturn(Optional.of(product));
//            when(deliveryProductRepo.saveAll(anyList())).thenReturn(List.of(deliveryProduct));
//            when(deliveryHistoryRepo.save(any(DeliveryHistory.class))).thenReturn(deliveryHistory);
//            when(deliveryMapper.toDetailsResponse(any(Delivery.class))).thenReturn(detailsResponse);
//
//            // When
//            DeliveryDetailsResponse result = deliveryService.createDelivery(createRequest, 1L);
//
//            // Then
//            assertThat(result).isNotNull();
//            verify(productRepo).findById(1L);
//            verify(deliveryProductRepo).saveAll(anyList());
//        }
//
//        @Test
//        @DisplayName("Should throw ResourceNotFoundException when sender not found")
//        void shouldThrowExceptionWhenSenderNotFound() {
//            // Given
//            when(senderRepo.findById(1L)).thenReturn(Optional.empty());
//
//            // When & Then
//            assertThatThrownBy(() -> deliveryService.createDelivery(createRequest, 1L))
//                    .isInstanceOf(ResourceNotFoundException.class)
//                    .hasMessage("Sender not found");
//
//            verify(deliveryRepo, never()).save(any());
//        }
//
//        @Test
//        @DisplayName("Should throw ZoneNotServicedException when pickup zone not found")
//        void shouldThrowExceptionWhenPickupZoneNotFound() {
//            // Given
//            when(senderRepo.findById(1L)).thenReturn(Optional.of(sender));
//            when(recipientService.findOrCreateRecipient(any())).thenReturn(recipient);
//            when(zonePostalCodeRepo.findZoneByPostalCode("10000")).thenReturn(Optional.empty());
//
//            // When & Then
//            assertThatThrownBy(() -> deliveryService.createDelivery(createRequest, 1L))
//                    .isInstanceOf(ZoneNotServicedException.class)
//                    .hasMessage("Pickup zone not available");
//
//            verify(deliveryRepo, never()).save(any());
//        }
//
//        @Test
//        @DisplayName("Should throw ZoneNotServicedException when shipping zone not found")
//        void shouldThrowExceptionWhenShippingZoneNotFound() {
//            // Given
//            when(senderRepo.findById(1L)).thenReturn(Optional.of(sender));
//            when(recipientService.findOrCreateRecipient(any())).thenReturn(recipient);
//            when(zonePostalCodeRepo.findZoneByPostalCode("10000")).thenReturn(Optional.of(pickupZone));
//            when(zonePostalCodeRepo.findZoneByPostalCode("20000")).thenReturn(Optional.empty());
//
//            // When & Then
//            assertThatThrownBy(() -> deliveryService.createDelivery(createRequest, 1L))
//                    .isInstanceOf(ZoneNotServicedException.class)
//                    .hasMessage("Shipping zone not available");
//
//            verify(deliveryRepo, never()).save(any());
//        }
//
//        @Test
//        @DisplayName("Should throw ResourceNotFoundException when product not found")
//        void shouldThrowExceptionWhenProductNotFound() {
//            // Given
//            CreateDeliveryRequest.DeliveryProductRequest productRequest = new CreateDeliveryRequest.DeliveryProductRequest();
//            productRequest.setProductId(999L);
//            productRequest.setQuantity(1);
//            createRequest.setProducts(List.of(productRequest));
//
//            when(senderRepo.findById(1L)).thenReturn(Optional.of(sender));
//            when(recipientService.findOrCreateRecipient(any())).thenReturn(recipient);
//            when(zonePostalCodeRepo.findZoneByPostalCode("10000")).thenReturn(Optional.of(pickupZone));
//            when(zonePostalCodeRepo.findZoneByPostalCode("20000")).thenReturn(Optional.of(shippingZone));
//            doNothing().when(zoneService).validateDeliveryZones(pickupZone, shippingZone);
//            when(deliveryMapper.toEntity(any(CreateDeliveryRequest.class))).thenReturn(delivery);
//            when(deliveryRepo.save(any(Delivery.class))).thenReturn(delivery);
//            when(productRepo.findById(999L)).thenReturn(Optional.empty());
//
//            // When & Then
//            assertThatThrownBy(() -> deliveryService.createDelivery(createRequest, 1L))
//                    .isInstanceOf(ResourceNotFoundException.class)
//                    .hasMessage("Product not found");
//        }
//    }
//
//    // ==================== GET DELIVERY BY ID TESTS ====================
//
//    @Nested
//    @DisplayName("getDeliveryById()")
//    class GetDeliveryByIdTests {
//
//        @Test
//        @DisplayName("Should return delivery details when found")
//        void shouldReturnDeliveryDetailsWhenFound() {
//            // Given
//            when(deliveryRepo.findById(1L)).thenReturn(Optional.of(delivery));
//            when(deliveryMapper.toDetailsResponse(delivery)).thenReturn(detailsResponse);
//
//            // When
//            DeliveryDetailsResponse result = deliveryService.getDeliveryById(1L);
//
//            // Then
//            assertThat(result).isNotNull();
//            assertThat(result.getId()).isEqualTo(1L);
//            verify(deliveryRepo).findById(1L);
//        }
//
//        @Test
//        @DisplayName("Should throw ResourceNotFoundException when delivery not found")
//        void shouldThrowExceptionWhenDeliveryNotFound() {
//            // Given
//            when(deliveryRepo.findById(1L)).thenReturn(Optional.empty());
//
//            // When & Then
//            assertThatThrownBy(() -> deliveryService.getDeliveryById(1L))
//                    .isInstanceOf(ResourceNotFoundException.class)
//                    .hasMessage("Delivery not found");
//        }
//    }
//
//    // ==================== GET DELIVERY WITH OWNERSHIP CHECK TESTS ====================
//
//    @Nested
//    @DisplayName("getDeliveryByIdWithOwnershipCheck()")
//    class GetDeliveryByIdWithOwnershipCheckTests {
//
//        @Test
//        @DisplayName("Should return delivery for assigned collecting courier")
//        void shouldReturnDeliveryForAssignedCollectingCourier() {
//            // Given
//            delivery.setCollectingCourier(collectingCourier);
//            when(deliveryRepo.findById(1L)).thenReturn(Optional.of(delivery));
//            when(deliveryMapper.toDetailsResponse(delivery)).thenReturn(detailsResponse);
//
//            // When
//            DeliveryDetailsResponse result = deliveryService.getDeliveryByIdWithOwnershipCheck(1L, 1L, "ROLE_COURIER");
//
//            // Then
//            assertThat(result).isNotNull();
//        }
//
//        @Test
//        @DisplayName("Should return delivery for assigned shipping courier")
//        void shouldReturnDeliveryForAssignedShippingCourier() {
//            // Given
//            delivery.setShippingCourier(shippingCourier);
//            when(deliveryRepo.findById(1L)).thenReturn(Optional.of(delivery));
//            when(deliveryMapper.toDetailsResponse(delivery)).thenReturn(detailsResponse);
//
//            // When
//            DeliveryDetailsResponse result = deliveryService.getDeliveryByIdWithOwnershipCheck(1L, 2L, "ROLE_COURIER");
//
//            // Then
//            assertThat(result).isNotNull();
//        }
//
//        @Test
//        @DisplayName("Should throw AccessDeniedException for unassigned courier")
//        void shouldThrowExceptionForUnassignedCourier() {
//            // Given
//            delivery.setCollectingCourier(collectingCourier);
//            delivery.setShippingCourier(shippingCourier);
//            when(deliveryRepo.findById(1L)).thenReturn(Optional.of(delivery));
//
//            // When & Then
//            assertThatThrownBy(() -> deliveryService.getDeliveryByIdWithOwnershipCheck(1L, 999L, "ROLE_COURIER"))
//                    .isInstanceOf(AccessDeniedException.class)
//                    .hasMessage("Access denied");
//        }
//
//        @Test
//        @DisplayName("Should return delivery for sender who owns it")
//        void shouldReturnDeliveryForOwnerSender() {
//            // Given
//            when(deliveryRepo.findById(1L)).thenReturn(Optional.of(delivery));
//            when(deliveryMapper.toDetailsResponse(delivery)).thenReturn(detailsResponse);
//
//            // When
//            DeliveryDetailsResponse result = deliveryService.getDeliveryByIdWithOwnershipCheck(1L, 1L, "ROLE_SENDER");
//
//            // Then
//            assertThat(result).isNotNull();
//        }
//
//        @Test
//        @DisplayName("Should throw AccessDeniedException for sender who doesn't own delivery")
//        void shouldThrowExceptionForNonOwnerSender() {
//            // Given
//            when(deliveryRepo.findById(1L)).thenReturn(Optional.of(delivery));
//
//            // When & Then
//            assertThatThrownBy(() -> deliveryService.getDeliveryByIdWithOwnershipCheck(1L, 999L, "ROLE_SENDER"))
//                    .isInstanceOf(AccessDeniedException.class)
//                    .hasMessage("Access denied");
//        }
//    }
//
//    // ==================== GET DELIVERY TRACKING TESTS ====================
//
//    @Nested
//    @DisplayName("getDeliveryTracking()")
//    class GetDeliveryTrackingTests {
//
//        @Test
//        @DisplayName("Should return tracking info when found")
//        void shouldReturnTrackingInfoWhenFound() {
//            // Given
//            when(deliveryRepo.findByTrackingNumber("TRACK123456")).thenReturn(Optional.of(delivery));
//            when(deliveryMapper.toTrackingResponse(delivery)).thenReturn(trackingResponse);
//
//            // When
//            DeliveryTrackingResponse result = deliveryService.getDeliveryTracking("TRACK123456");
//
//            // Then
//            assertThat(result).isNotNull();
//            assertThat(result.getTrackingNumber()).isEqualTo("TRACK123456");
//        }
//
//        @Test
//        @DisplayName("Should throw ResourceNotFoundException when tracking number not found")
//        void shouldThrowExceptionWhenTrackingNotFound() {
//            // Given
//            when(deliveryRepo.findByTrackingNumber("INVALID")).thenReturn(Optional.empty());
//
//            // When & Then
//            assertThatThrownBy(() -> deliveryService.getDeliveryTracking("INVALID"))
//                    .isInstanceOf(ResourceNotFoundException.class)
//                    .hasMessage("Delivery not found");
//        }
//    }
//
//    // ==================== UPDATE DELIVERY STATUS TESTS ====================
//
//    @Nested
//    @DisplayName("updateDeliveryStatus()")
//    class UpdateDeliveryStatusTests {
//
//        @Test
//        @DisplayName("Should update delivery status successfully")
//        void shouldUpdateDeliveryStatusSuccessfully() {
//            // Given
//            when(deliveryRepo.findById(1L)).thenReturn(Optional.of(delivery));
//            when(deliveryRepo.save(any(Delivery.class))).thenReturn(delivery);
//            when(deliveryHistoryRepo.save(any(DeliveryHistory.class))).thenReturn(deliveryHistory);
//            when(deliveryMapper.toDetailsResponse(any(Delivery.class))).thenReturn(detailsResponse);
//
//            // When
//            DeliveryDetailsResponse result = deliveryService.updateDeliveryStatus(1L, updateStatusRequest, 1L);
//
//            // Then
//            assertThat(result).isNotNull();
//            verify(deliveryRepo).save(any(Delivery.class));
//            verify(deliveryHistoryRepo).save(any(DeliveryHistory.class));
//        }
//
//        @Test
//        @DisplayName("Should throw ResourceNotFoundException when delivery not found")
//        void shouldThrowExceptionWhenDeliveryNotFound() {
//            // Given
//            when(deliveryRepo.findById(1L)).thenReturn(Optional.empty());
//
//            // When & Then
//            assertThatThrownBy(() -> deliveryService.updateDeliveryStatus(1L, updateStatusRequest, 1L))
//                    .isInstanceOf(ResourceNotFoundException.class)
//                    .hasMessage("Delivery not found");
//        }
//    }
//
//    // ==================== ASSIGN COLLECTING COURIER TESTS ====================
//
//    @Nested
//    @DisplayName("assignCollectingCourier()")
//    class AssignCollectingCourierTests {
//
//        @Test
//        @DisplayName("Should assign collecting courier successfully")
//        void shouldAssignCollectingCourierSuccessfully() {
//            // Given
//            // Courier zone different from pickup zone (zone mismatch check expects different zones)
//            Courier courier = new Courier();
//            courier.setId(1L);
//            courier.setFirstName("Collecting");
//            courier.setLastName("Courier");
//            courier.setZone(shippingZone); // Different zone
//
//            when(deliveryRepo.findById(1L)).thenReturn(Optional.of(delivery));
//            when(courierRepo.findById(1L)).thenReturn(Optional.of(courier));
//            when(deliveryRepo.save(any(Delivery.class))).thenReturn(delivery);
//            when(deliveryHistoryRepo.save(any(DeliveryHistory.class))).thenReturn(deliveryHistory);
//            when(deliveryMapper.toDetailsResponse(any(Delivery.class))).thenReturn(detailsResponse);
//
//            // When
//            DeliveryDetailsResponse result = deliveryService.assignCollectingCourier(1L, assignRequest);
//
//            // Then
//            assertThat(result).isNotNull();
//            verify(deliveryRepo).save(any(Delivery.class));
//        }
//
//        @Test
//        @DisplayName("Should throw exception when courier zone matches pickup zone")
//        void shouldThrowExceptionWhenCourierZoneMatchesPickupZone() {
//            // Given - Courier in same zone as pickup (which triggers the mismatch exception)
//            when(deliveryRepo.findById(1L)).thenReturn(Optional.of(delivery));
//            when(courierRepo.findById(1L)).thenReturn(Optional.of(collectingCourier));
//
//            // When & Then
//            assertThatThrownBy(() -> deliveryService.assignCollectingCourier(1L, assignRequest))
//                    .isInstanceOf(DeliveryCourierZoneMismatchException.class);
//
//            verify(deliveryRepo, never()).save(any());
//        }
//
//        @Test
//        @DisplayName("Should throw ResourceNotFoundException when courier not found")
//        void shouldThrowExceptionWhenCourierNotFound() {
//            // Given
//            when(deliveryRepo.findById(1L)).thenReturn(Optional.of(delivery));
//            when(courierRepo.findById(1L)).thenReturn(Optional.empty());
//
//            // When & Then
//            assertThatThrownBy(() -> deliveryService.assignCollectingCourier(1L, assignRequest))
//                    .isInstanceOf(ResourceNotFoundException.class)
//                    .hasMessage("Courier not found");
//        }
//    }
//
//    // ==================== ASSIGN SHIPPING COURIER TESTS ====================
//
//    @Nested
//    @DisplayName("assignShippingCourier()")
//    class AssignShippingCourierTests {
//
//        @Test
//        @DisplayName("Should assign shipping courier successfully")
//        void shouldAssignShippingCourierSuccessfully() {
//            // Given
//            Courier courier = new Courier();
//            courier.setId(2L);
//            courier.setFirstName("Shipping");
//            courier.setLastName("Courier");
//            courier.setZone(shippingZone); // Different zone from pickup
//
//            assignRequest.setCourierId(2L);
//
//            when(deliveryRepo.findById(1L)).thenReturn(Optional.of(delivery));
//            when(courierRepo.findById(2L)).thenReturn(Optional.of(courier));
//            when(deliveryRepo.save(any(Delivery.class))).thenReturn(delivery);
//            when(deliveryHistoryRepo.save(any(DeliveryHistory.class))).thenReturn(deliveryHistory);
//            when(deliveryMapper.toDetailsResponse(any(Delivery.class))).thenReturn(detailsResponse);
//
//            // When
//            DeliveryDetailsResponse result = deliveryService.assignShippingCourier(1L, assignRequest);
//
//            // Then
//            assertThat(result).isNotNull();
//            verify(deliveryRepo).save(any(Delivery.class));
//        }
//
//        @Test
//        @DisplayName("Should throw ResourceNotFoundException when courier not found")
//        void shouldThrowExceptionWhenCourierNotFound() {
//            // Given
//            when(deliveryRepo.findById(1L)).thenReturn(Optional.of(delivery));
//            when(courierRepo.findById(1L)).thenReturn(Optional.empty());
//
//            // When & Then
//            assertThatThrownBy(() -> deliveryService.assignShippingCourier(1L, assignRequest))
//                    .isInstanceOf(ResourceNotFoundException.class)
//                    .hasMessage("Courier not found");
//        }
//    }
//
//    // ==================== SEARCH DELIVERIES TESTS ====================
//
//    @Nested
//    @DisplayName("searchDeliveries()")
//    class SearchDeliveriesTests {
//
//        @Test
//        @DisplayName("Should return paginated search results")
//        void shouldReturnPaginatedSearchResults() {
//            // Given
//            Pageable pageable = PageRequest.of(0, 10);
//            List<Delivery> deliveryList = List.of(delivery);
//            Page<Delivery> deliveryPage = new PageImpl<>(deliveryList, pageable, 1);
//
//            when(deliveryRepo.findAll(any(Specification.class), eq(pageable))).thenReturn(deliveryPage);
//            when(deliveryMapper.toResponse(any(Delivery.class))).thenReturn(deliveryResponse);
//
//            // When
//            Page<DeliveryResponse> result = deliveryService.searchDeliveries(searchRequest, pageable);
//
//            // Then
//            assertThat(result).isNotNull();
//            assertThat(result.getContent()).hasSize(1);
//        }
//
//        @Test
//        @DisplayName("Should return empty page when no deliveries match")
//        void shouldReturnEmptyPageWhenNoDeliveriesMatch() {
//            // Given
//            Pageable pageable = PageRequest.of(0, 10);
//            Page<Delivery> emptyPage = new PageImpl<>(List.of(), pageable, 0);
//
//            when(deliveryRepo.findAll(any(Specification.class), eq(pageable))).thenReturn(emptyPage);
//
//            // When
//            Page<DeliveryResponse> result = deliveryService.searchDeliveries(searchRequest, pageable);
//
//            // Then
//            assertThat(result).isNotNull();
//            assertThat(result.getContent()).isEmpty();
//        }
//    }
//
//    // ==================== GET DELIVERIES BY SENDER TESTS ====================
//
//    @Nested
//    @DisplayName("getDeliveriesBySender()")
//    class GetDeliveriesBySenderTests {
//
//        @Test
//        @DisplayName("Should return sender's deliveries")
//        void shouldReturnSendersDeliveries() {
//            // Given
//            Pageable pageable = PageRequest.of(0, 10);
//            Page<Delivery> deliveryPage = new PageImpl<>(List.of(delivery), pageable, 1);
//
//            when(deliveryRepo.findBySenderId(1L, pageable)).thenReturn(deliveryPage);
//            when(deliveryMapper.toResponse(any(Delivery.class))).thenReturn(deliveryResponse);
//
//            // When
//            Page<DeliveryResponse> result = deliveryService.getDeliveriesBySender(1L, pageable);
//
//            // Then
//            assertThat(result).isNotNull();
//            assertThat(result.getContent()).hasSize(1);
//            verify(deliveryRepo).findBySenderId(1L, pageable);
//        }
//    }
//
//    // ==================== GET DELIVERIES BY RECIPIENT TESTS ====================
//
//    @Nested
//    @DisplayName("getDeliveriesByRecipient()")
//    class GetDeliveriesByRecipientTests {
//
//        @Test
//        @DisplayName("Should return recipient's deliveries")
//        void shouldReturnRecipientsDeliveries() {
//            // Given
//            Pageable pageable = PageRequest.of(0, 10);
//            Page<Delivery> deliveryPage = new PageImpl<>(List.of(delivery), pageable, 1);
//
//            when(deliveryRepo.findByRecipientPhone("0612345678", pageable)).thenReturn(deliveryPage);
//            when(deliveryMapper.toResponse(any(Delivery.class))).thenReturn(deliveryResponse);
//
//            // When
//            Page<DeliveryResponse> result = deliveryService.getDeliveriesByRecipient("0612345678", pageable);
//
//            // Then
//            assertThat(result).isNotNull();
//            assertThat(result.getContent()).hasSize(1);
//            verify(deliveryRepo).findByRecipientPhone("0612345678", pageable);
//        }
//    }
//
//    // ==================== GET DELIVERIES BY COURIER TESTS ====================
//
//    @Nested
//    @DisplayName("getDeliveriesByCourier()")
//    class GetDeliveriesByCourierTests {
//
//        @Test
//        @DisplayName("Should return courier's deliveries")
//        void shouldReturnCouriersDeliveries() {
//            // Given
//            Pageable pageable = PageRequest.of(0, 10);
//            Page<Delivery> deliveryPage = new PageImpl<>(List.of(delivery), pageable, 1);
//
//            when(deliveryRepo.findByCollectingCourierIdOrShippingCourierId(1L, 1L, pageable)).thenReturn(deliveryPage);
//            when(deliveryMapper.toResponse(any(Delivery.class))).thenReturn(deliveryResponse);
//
//            // When
//            Page<DeliveryResponse> result = deliveryService.getDeliveriesByCourier(1L, pageable);
//
//            // Then
//            assertThat(result).isNotNull();
//            assertThat(result.getContent()).hasSize(1);
//        }
//    }
//
//    // ==================== GET MY DELIVERIES TESTS ====================
//
//    @Nested
//    @DisplayName("getMyDeliveries()")
//    class GetMyDeliveriesTests {
//
//        @Test
//        @DisplayName("Should return courier's own deliveries")
//        void shouldReturnCouriersOwnDeliveries() {
//            // Given
//            Pageable pageable = PageRequest.of(0, 10);
//            Page<Delivery> deliveryPage = new PageImpl<>(List.of(delivery), pageable, 1);
//
//            when(deliveryRepo.findByCollectingCourierIdOrShippingCourierId(1L, 1L, pageable)).thenReturn(deliveryPage);
//            when(deliveryMapper.toResponse(any(Delivery.class))).thenReturn(deliveryResponse);
//
//            // When
//            Page<DeliveryResponse> result = deliveryService.getMyDeliveries(1L, pageable);
//
//            // Then
//            assertThat(result).isNotNull();
//            assertThat(result.getContent()).hasSize(1);
//        }
//    }
//
//    // ==================== DELETE DELIVERY TESTS ====================
//
//    @Nested
//    @DisplayName("deleteDelivery()")
//    class DeleteDeliveryTests {
//
//        @Test
//        @DisplayName("Should delete delivery with CREATED status")
//        void shouldDeleteDeliveryWithCreatedStatus() {
//            // Given
//            delivery.setStatus(DeliveryStatus.CREATED);
//            when(deliveryRepo.findById(1L)).thenReturn(Optional.of(delivery));
//            doNothing().when(deliveryRepo).deleteById(1L);
//
//            // When
//            deliveryService.deleteDelivery(1L);
//
//            // Then
//            verify(deliveryRepo).deleteById(1L);
//        }
//
//        @Test
//        @DisplayName("Should delete delivery with DELIVERED status")
//        void shouldDeleteDeliveryWithDeliveredStatus() {
//            // Given
//            delivery.setStatus(DeliveryStatus.DELIVERED);
//            when(deliveryRepo.findById(1L)).thenReturn(Optional.of(delivery));
//            doNothing().when(deliveryRepo).deleteById(1L);
//
//            // When
//            deliveryService.deleteDelivery(1L);
//
//            // Then
//            verify(deliveryRepo).deleteById(1L);
//        }
//
//        @Test
//        @DisplayName("Should throw RuntimeException when deleting IN_TRANSIT delivery")
//        void shouldThrowExceptionWhenDeletingInTransitDelivery() {
//            // Given
//            delivery.setStatus(DeliveryStatus.IN_TRANSIT);
//            when(deliveryRepo.findById(1L)).thenReturn(Optional.of(delivery));
//
//            // When & Then
//            assertThatThrownBy(() -> deliveryService.deleteDelivery(1L))
//                    .isInstanceOf(RuntimeException.class)
//                    .hasMessage("Couldn't delete a undelievered delivery!");
//
//            verify(deliveryRepo, never()).deleteById(anyLong());
//        }
//
//        @Test
//        @DisplayName("Should throw ResourceNotFoundException when delivery not found")
//        void shouldThrowExceptionWhenDeliveryNotFound() {
//            // Given
//            when(deliveryRepo.findById(1L)).thenReturn(Optional.empty());
//
//            // When & Then
//            assertThatThrownBy(() -> deliveryService.deleteDelivery(1L))
//                    .isInstanceOf(ResourceNotFoundException.class)
//                    .hasMessage("Delivery not found");
//
//            verify(deliveryRepo, never()).deleteById(anyLong());
//        }
//    }
//}