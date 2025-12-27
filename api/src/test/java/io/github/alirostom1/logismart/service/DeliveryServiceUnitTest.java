//package io.github.alirostom1.logismart.service;
//
//import io.github.alirostom1.logismart.dto.request.delivery.AssignDeliveryRequest;
//import io.github.alirostom1.logismart.dto.request.delivery.CreateDeliveryRequest;
//import io.github.alirostom1.logismart.dto.request.delivery.SearchDeliveryRequest;
//import io.github.alirostom1.logismart.dto.request.delivery.UpdateDeliveryStatusRequest;
//import io.github.alirostom1.logismart.dto.response.delivery.DeliveryDetailsResponse;
//import io.github.alirostom1.logismart.dto.response.delivery.DeliveryResponse;
//import io.github.alirostom1.logismart.dto.response.delivery.DeliveryTrackingResponse;
//import io.github.alirostom1.logismart.exception.ResourceNotFoundException;
//import io.github.alirostom1.logismart.mapper.DeliveryMapper;
//import io.github.alirostom1.logismart.mapper.DeliveryProductMapper;
//import io.github.alirostom1.logismart.model.entity.*;
//import io.github.alirostom1.logismart.model.enums.DeliveryStatus;
//import io.github.alirostom1.logismart.repository.*;
//import org.aspectj.lang.annotation.Before;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mapstruct.factory.Mappers;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.*;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//public class DeliveryServiceUnitTest {
//
//    @Mock DeliveryRepo deliveryRepo;
//    @Mock PersonRepo personRepo;
//    @Mock ZoneRepo zoneRepo;
//    @Mock DeliveryHistoryRepo deliveryHistoryRepo;
//    @Mock CourierRepo courierRepo;
//    @Mock ProductRepo productRepo;
//    @Mock DeliveryProductRepo deliveryProductRepo;
//
//    @Autowired
//    DeliveryMapper deliveryMapper;
//    DeliveryService deliveryService;
//
//    @BeforeEach
//    public void setup(){
//        deliveryService = new DeliveryService(deliveryRepo,deliveryMapper,personRepo,zoneRepo,
//                deliveryHistoryRepo,courierRepo,
//                productRepo,deliveryProductRepo
//        );
//    }
//
//
//    // CREATE DELIVERY
//    @Test
//    public void createDelivery_should_return_details_response() {
//        UUID senderUuid = UUID.randomUUID();
//        UUID recipientUuid = UUID.randomUUID();
//        UUID zoneUuid = UUID.randomUUID();
//        UUID productUuid = UUID.randomUUID();
//
//        CreateDeliveryRequest request = new CreateDeliveryRequest(
//                "description1","city1",30.0,"HIGH",
//                senderUuid.toString(),
//                recipientUuid.toString(),
//                zoneUuid.toString(),
//                List.of(new CreateDeliveryRequest.DeliveryProductRequest(
//                        productUuid.toString(),
//                        3
//                ))
//        );
//
//        Person senderPerson = new Sender();
//        senderPerson.setId(senderUuid);
//        Person recipientPerson = new Recipient();
//        recipientPerson.setId(recipientUuid);
//
//        Sender sender = (Sender) senderPerson;
//        Recipient recipient = (Recipient) recipientPerson;
//
//        Zone zone = new Zone();
//        zone.setId(zoneUuid);
//
//        Product product = new Product();
//        product.setId(productUuid);
//        product.setUnitPrice(10.0);
//
//        Delivery delivery = deliveryMapper.toEntity(request);
//
//        delivery.setSender(sender);
//        delivery.setRecipient(recipient);
//        delivery.setZone(zone);
//
//        DeliveryProduct deliveryProduct = new DeliveryProduct();
//        deliveryProduct.setDelivery(delivery);
//        deliveryProduct.setProduct(product);
//        deliveryProduct.setQuantity(3);
//        deliveryProduct.setPrice(30.0);
//
//        List<DeliveryProduct> deliveryProducts = List.of(deliveryProduct);
//        delivery.setDeliveryProducts(deliveryProducts);
//
//        DeliveryHistory deliveryHistory = new DeliveryHistory();
//        deliveryHistory.setStatus(DeliveryStatus.CREATED);
//        deliveryHistory.setComment("Delivery created");
//
//        when(personRepo.findById(eq(senderUuid))).thenReturn(Optional.of(senderPerson));
//        when(personRepo.findById(eq(recipientUuid))).thenReturn(Optional.of(recipientPerson));
//        when(zoneRepo.findById(eq(zoneUuid))).thenReturn(Optional.of(zone));
//        when(productRepo.findById(eq(productUuid))).thenReturn(Optional.of(product));
//        when(deliveryRepo.save(any(Delivery.class))).thenReturn(delivery);
//        when(deliveryProductRepo.saveAll(anyList())).thenReturn(deliveryProducts);
//        when(deliveryHistoryRepo.save(any(DeliveryHistory.class))).thenReturn(deliveryHistory);
//
//        DeliveryDetailsResponse response = deliveryService.createDelivery(request);
//
//        delivery.setDeliveryHistoryList(new ArrayList<>(List.of(deliveryHistory)));
//        DeliveryDetailsResponse expectedResponse = deliveryMapper.toDetailsResponse(delivery);
//
//
//        assertEquals(expectedResponse,response);
//    }
//
//    @Test
//    public void createDelivery_should_return_details_response_no_products(){
//        UUID senderUuid = UUID.randomUUID();
//        UUID recipientUuid = UUID.randomUUID();
//        UUID zoneUuid = UUID.randomUUID();
//        CreateDeliveryRequest request = new CreateDeliveryRequest(
//                "description1","city1",30.0,"HIGH",
//                senderUuid.toString(),
//                recipientUuid.toString(),
//                zoneUuid.toString(),
//                null
//        );
//        Person senderPerson = new Sender();
//        senderPerson.setId(senderUuid);
//        Person recipientPerson = new Recipient();
//        recipientPerson.setId(recipientUuid);
//
//        Sender sender = (Sender) senderPerson;
//        Recipient recipient = (Recipient) recipientPerson;
//
//        Zone zone = new Zone();
//        zone.setId(zoneUuid);
//        Delivery delivery = deliveryMapper.toEntity(request);
//
//        delivery.setSender(sender);
//        delivery.setRecipient(recipient);
//        delivery.setZone(zone);
//
//        DeliveryHistory deliveryHistory = new DeliveryHistory();
//        deliveryHistory.setStatus(DeliveryStatus.CREATED);
//        deliveryHistory.setComment("Delivery created");
//
//        when(personRepo.findById(eq(senderUuid))).thenReturn(Optional.of(senderPerson));
//        when(personRepo.findById(eq(recipientUuid))).thenReturn(Optional.of(recipientPerson));
//        when(zoneRepo.findById(eq(zoneUuid))).thenReturn(Optional.of(zone));
//        when(deliveryRepo.save(any(Delivery.class))).thenReturn(delivery);
//        when(deliveryHistoryRepo.save(any(DeliveryHistory.class))).thenReturn(deliveryHistory);
//
//        DeliveryDetailsResponse response = deliveryService.createDelivery(request);
//
//        delivery.setDeliveryHistoryList(new ArrayList<>(List.of(deliveryHistory)));
//        DeliveryDetailsResponse expectedResponse = deliveryMapper.toDetailsResponse(delivery);
//        assertEquals(expectedResponse,response);
//    }
//    @Test
//    public void createDelivery_should_throw_not_found_sender() {
//        String senderId = UUID.randomUUID().toString();
//        Person sender = new Recipient();
//        String recipientId = UUID.randomUUID().toString();
//        String zoneId = UUID.randomUUID().toString();
//        Person recipient = new Recipient();
//        CreateDeliveryRequest request = new CreateDeliveryRequest("delivery1","city1",50.0,"HIGH",senderId, recipientId, zoneId, List.of());
//
//        when(personRepo.findById(UUID.fromString(senderId))).thenReturn(Optional.of(sender));
//        when(personRepo.findById(UUID.fromString(recipientId))).thenReturn(Optional.of(recipient));
//
//        assertThrows(ResourceNotFoundException.class, () -> deliveryService.createDelivery(request));
//    }
//    @Test
//    public void createDelivery_should_throw_not_found_recipient() {
//        String senderId = UUID.randomUUID().toString();
//        Person sender = new Sender();
//        Person recipient = new Sender();
//        String recipientId = UUID.randomUUID().toString();
//        String zoneId = UUID.randomUUID().toString();
//        CreateDeliveryRequest request = new CreateDeliveryRequest("delivery1","city1",50.0,"HIGH",senderId, recipientId, zoneId, List.of());
//
//        when(personRepo.findById(UUID.fromString(senderId))).thenReturn(Optional.of(sender));
//        when(personRepo.findById(UUID.fromString(recipientId))).thenReturn(Optional.of(recipient));
//
//
//        assertThrows(ResourceNotFoundException.class, () -> deliveryService.createDelivery(request));
//    }
//
//    @Test
//    public void createDelivery_should_throw_for_missing_sender() {
//        String senderId = UUID.randomUUID().toString();
//        String recipientId = UUID.randomUUID().toString();
//        String zoneId = UUID.randomUUID().toString();
//        CreateDeliveryRequest request = new CreateDeliveryRequest("delivery1","city1",50.0,"HIGH",senderId, recipientId, zoneId, List.of());
//
//        when(personRepo.findById(UUID.fromString(senderId))).thenReturn(Optional.empty());
//
//        assertThrows(ResourceNotFoundException.class, () -> deliveryService.createDelivery(request));
//    }
//    @Test
//    public void createDelivery_should_throw_for_missing_recipient() {
//        String senderId = UUID.randomUUID().toString();
//        String recipientId = UUID.randomUUID().toString();
//        String zoneId = UUID.randomUUID().toString();
//        CreateDeliveryRequest request = new CreateDeliveryRequest("delivery1","city1",50.0,"HIGH",senderId, recipientId, zoneId, List.of());
//        Person sender = new Sender();
//        when(personRepo.findById(UUID.fromString(senderId))).thenReturn(Optional.of(sender));
//        when(personRepo.findById(UUID.fromString(recipientId))).thenReturn(Optional.empty());
//
//
//        assertThrows(ResourceNotFoundException.class, () -> deliveryService.createDelivery(request));
//    }
//    @Test
//    public void createDelivery_should_throw_zone_not_found() {
//        String senderId = UUID.randomUUID().toString();
//        String recipientId = UUID.randomUUID().toString();
//        String zoneId = UUID.randomUUID().toString();
//        String productId = UUID.randomUUID().toString();
//        CreateDeliveryRequest request = new CreateDeliveryRequest("delivery1","city1",50.0,"HIGH",senderId, recipientId, zoneId, List.of());
//        Person sender = new Sender();
//        Person recipient = new Recipient();
//        when(personRepo.findById(UUID.fromString(senderId))).thenReturn(Optional.of(sender));
//        when(personRepo.findById(UUID.fromString(recipientId))).thenReturn(Optional.of(recipient));
//        when(personRepo.findById(UUID.fromString(senderId))).thenReturn(Optional.of(sender));
//        when(zoneRepo.findById(UUID.fromString(zoneId))).thenReturn(Optional.empty());
//
//
//
//        Exception ex = assertThrows(ResourceNotFoundException.class, () -> deliveryService.createDelivery(request));
//        assertTrue(ex.getMessage().contains("Zone"));
//    }
//
//    @Test
//    public void createDelivery_should_throw_product_not_found() {
//        String senderId = UUID.randomUUID().toString();
//        String recipientId = UUID.randomUUID().toString();
//        String zoneId = UUID.randomUUID().toString();
//        String productId = UUID.randomUUID().toString();
//        CreateDeliveryRequest request = new CreateDeliveryRequest("delivery1","city1",50.0,"HIGH",senderId, recipientId, zoneId
//                , List.of(new CreateDeliveryRequest.DeliveryProductRequest(productId,2)));
//        Person sender = new Sender();
//        Person recipient = new Recipient();
//        Zone zone = new Zone();
//        Delivery delivery = new Delivery();
//        when(personRepo.findById(UUID.fromString(senderId))).thenReturn(Optional.of(sender));
//        when(personRepo.findById(UUID.fromString(recipientId))).thenReturn(Optional.of(recipient));
//        when(personRepo.findById(UUID.fromString(senderId))).thenReturn(Optional.of(sender));
//        when(zoneRepo.findById(UUID.fromString(zoneId))).thenReturn(Optional.of(zone));
//        when(deliveryRepo.save(any(Delivery.class))).thenReturn(delivery);
//        when(productRepo.findById(UUID.fromString(productId))).thenReturn(Optional.empty());
//
//
//
//        Exception ex = assertThrows(ResourceNotFoundException.class, () -> deliveryService.createDelivery(request));
//        assertTrue(ex.getMessage().contains("Product"));
//    }
//
//
//    // GET DELIVERY BY ID
//    @Test
//    public void getDeliveryById_should_return_details_response() {
//        UUID deliveryId = UUID.randomUUID();
//        Delivery delivery = new Delivery();
//        delivery.setId(deliveryId);
//        DeliveryDetailsResponse expected = deliveryMapper.toDetailsResponse(delivery);
//
//        when(deliveryRepo.findById(deliveryId)).thenReturn(Optional.of(delivery));
//
//        DeliveryDetailsResponse response = deliveryService.getDeliveryById(deliveryId.toString());
//        assertEquals(expected, response);
//    }
//
//    @Test
//    public void getDeliveryById_should_throw_when_not_found() {
//        UUID deliveryId = UUID.randomUUID();
//        when(deliveryRepo.findById(deliveryId)).thenReturn(Optional.empty());
//        assertThrows(ResourceNotFoundException.class, () -> deliveryService.getDeliveryById(deliveryId.toString()));
//    }
//
//    // TRACK DELIVERY
//    @Test
//    public void getDeliveryTracking_should_return_tracking_response() {
//        UUID deliveryId = UUID.randomUUID();
//        Delivery delivery = new Delivery();
//        Sender sender = new Sender();
//        Recipient recipient = new Recipient();
//        delivery.setId(deliveryId);
//        delivery.setSender(sender);
//        delivery.setRecipient(recipient);
//
//        DeliveryTrackingResponse expected = deliveryMapper.toTrackingResponse(delivery);
//
//        when(deliveryRepo.findById(deliveryId)).thenReturn(Optional.of(delivery));
//
//        DeliveryTrackingResponse response = deliveryService.getDeliveryTracking(deliveryId.toString());
//        assertEquals(expected, response);
//    }
//
//    // UPDATE DELIVERY STATUS
//    @Test
//    public void updateDeliveryStatus_should_update_and_return_details() {
//        UUID deliveryId = UUID.randomUUID();
//        Delivery delivery = new Delivery();
//        delivery.setId(deliveryId);
//        delivery.setStatus(DeliveryStatus.CREATED);
//
//
//        UpdateDeliveryStatusRequest request = new UpdateDeliveryStatusRequest("DELIVERED", "Delivered successfully");
//
//
//
//        when(deliveryRepo.findById(deliveryId)).thenReturn(Optional.of(delivery));
//        when(deliveryRepo.save(any(Delivery.class))).thenReturn(delivery);
//
//
//
//        DeliveryDetailsResponse response = deliveryService.updateDeliveryStatus(deliveryId.toString(), request);
//
//        assertTrue(response.getStatus() == DeliveryStatus.DELIVERED.toString());
//        verify(deliveryRepo).save(any(Delivery.class));
//        verify(deliveryHistoryRepo).save(any(DeliveryHistory.class));
//    }
//
//    // ASSIGN COURIERS
//    @Test
//    public void assignCollectingCourier_should_assign_and_return_details() {
//        UUID deliveryId = UUID.randomUUID();
//        String courierId = UUID.randomUUID().toString();
//        Delivery delivery = new Delivery();
//        delivery.setId(deliveryId);
//        Courier courier = new Courier();
//        courier.setId(UUID.fromString(courierId));
//
//        AssignDeliveryRequest request = new AssignDeliveryRequest(courierId);
//
//        when(deliveryRepo.findById(deliveryId)).thenReturn(Optional.of(delivery));
//        when(courierRepo.findById(UUID.fromString(courierId))).thenReturn(Optional.of(courier));
//        when(deliveryRepo.save(any(Delivery.class))).thenReturn(delivery);
//        when(deliveryHistoryRepo.save(any(DeliveryHistory.class))).thenReturn(new DeliveryHistory());
//
//        DeliveryDetailsResponse response = deliveryService.assignCollectingCourier(deliveryId.toString(), request);
//
//        assertEquals(courierId.toString(), response.getCollectingCourier().getId());
//    }
//    @Test
//    public void assignShippingCourier_should_assign_and_return_details() {
//        UUID deliveryId = UUID.randomUUID();
//        String courierId = UUID.randomUUID().toString();
//        Delivery delivery = new Delivery();
//        delivery.setId(deliveryId);
//        Courier courier = new Courier();
//        courier.setId(UUID.fromString(courierId));
//
//        AssignDeliveryRequest request = new AssignDeliveryRequest(courierId);
//
//        when(deliveryRepo.findById(deliveryId)).thenReturn(Optional.of(delivery));
//        when(courierRepo.findById(UUID.fromString(courierId))).thenReturn(Optional.of(courier));
//        when(deliveryRepo.save(any(Delivery.class))).thenReturn(delivery);
//        when(deliveryHistoryRepo.save(any(DeliveryHistory.class))).thenReturn(new DeliveryHistory());
//
//        DeliveryDetailsResponse response = deliveryService.assignShippingCourier(deliveryId.toString(), request);
//
//        assertEquals(courierId.toString(), response.getShippingCourier().getId());
//    }
//    @Test
//    public void assignCollectingCourier_should_throw_not_found(){
//        UUID deliveryId = UUID.randomUUID();
//        UUID courierId = UUID.randomUUID();
//        Delivery delivery = new Delivery();
//        AssignDeliveryRequest request = new AssignDeliveryRequest(courierId.toString());
//        when(deliveryRepo.findById(deliveryId)).thenReturn(Optional.of(delivery));
//        when(courierRepo.findById(courierId)).thenReturn(Optional.empty());
//        RuntimeException exception = assertThrows(ResourceNotFoundException.class,() -> deliveryService.assignCollectingCourier(deliveryId.toString(),request));
//        assertTrue(exception.getMessage().contains("Courier"));
//    }
//    @Test
//    public void assignShippingCourier_should_throw_not_found(){
//        UUID deliveryId = UUID.randomUUID();
//        UUID courierId = UUID.randomUUID();
//        Delivery delivery = new Delivery();
//        AssignDeliveryRequest request = new AssignDeliveryRequest(courierId.toString());
//        when(deliveryRepo.findById(deliveryId)).thenReturn(Optional.of(delivery));
//        when(courierRepo.findById(courierId)).thenReturn(Optional.empty());
//        RuntimeException exception = assertThrows(ResourceNotFoundException.class,() -> deliveryService.assignShippingCourier(deliveryId.toString(),request));
//        assertTrue(exception.getMessage().contains("Courier"));
//    }
//
//
//
//
//    @Test
//    public void searchDeliveries_should_return_page() {
//        SearchDeliveryRequest request = new SearchDeliveryRequest();
//        Pageable pageable = PageRequest.of(0, 10);
//        Delivery delivery = new Delivery();
//        Recipient recipient = new Recipient();
//        Sender sender = new Sender();
//        delivery.setSender(sender);
//        delivery.setRecipient(recipient);
//        Page<Delivery> page = new PageImpl<>(List.of(delivery), pageable, 1);
//
//        when(deliveryRepo.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
//
//        Page<DeliveryResponse> response = deliveryService.searchDeliveries(request, pageable);
//        assertTrue(response.getContent().size() == 1);
//    }
//
//    // GET DELIVERIES BY SENDER
//    @Test
//    public void getDeliveriesBySender_should_return_page() {
//        String senderId = UUID.randomUUID().toString();
//        Pageable pageable = PageRequest.of(0, 10);
//        Delivery delivery = new Delivery();
//        Sender sender = new Sender();
//        Recipient recipient = new Recipient();
//        delivery.setSender(sender);
//        delivery.setRecipient(recipient);
//        Page<Delivery> page = new PageImpl<>(List.of(delivery), pageable, 1);
//        when(deliveryRepo.findBySenderId(UUID.fromString(senderId), pageable)).thenReturn(page);
//
//        Page<DeliveryResponse> expected = page.map(deliveryMapper::toResponse);
//
//        Page<DeliveryResponse> response = deliveryService.getDeliveriesBySender(senderId, pageable);
//        assertEquals(expected, response);
//    }
//
//    // GET DELIVERIES BY RECIPIENT
//    @Test
//    public void getDeliveriesByRecipient_should_return_page() {
//        String recipientId = UUID.randomUUID().toString();
//        Pageable pageable = PageRequest.of(0, 10);
//        Delivery delivery = new Delivery();
//        Sender sender = new Sender();
//        Recipient recipient = new Recipient();
//        delivery.setSender(sender);
//        delivery.setRecipient(recipient);
//        Page<Delivery> page = new PageImpl<>(List.of(delivery), pageable, 1);
//        when(deliveryRepo.findByRecipientId(UUID.fromString(recipientId), pageable)).thenReturn(page);
//
//        Page<DeliveryResponse> expected = page.map(deliveryMapper::toResponse);
//
//        Page<DeliveryResponse> response = deliveryService.getDeliveriesByRecipient(recipientId, pageable);
//        assertEquals(expected, response);
//    }
//
//    // GET DELIVERIES BY COURIER
//    @Test
//    public void getDeliveriesByCourier_should_return_page() {
//        String courierId = UUID.randomUUID().toString();
//        Pageable pageable = PageRequest.of(0, 10);
//        Delivery delivery = new Delivery();
//        Sender sender = new Sender();
//        Recipient recipient = new Recipient();
//        delivery.setSender(sender);
//        delivery.setRecipient(recipient);
//        Page<Delivery> page = new PageImpl<>(List.of(delivery), pageable, 1);
//        UUID uuid = UUID.fromString(courierId);
//        when(deliveryRepo.findByCollectingCourierIdOrShippingCourierId(uuid, uuid, pageable)).thenReturn(page);
//
//        Page<DeliveryResponse> expected = page.map(deliveryMapper::toResponse);
//
//        Page<DeliveryResponse> response = deliveryService.getDeliveriesByCourier(courierId, pageable);
//        assertEquals(expected, response);
//    }
//
//    // DELETE DELIVERY
//    @Test
//    public void deleteDelivery_should_delete_when_status_delivered_or_created() {
//        UUID deliveryId = UUID.randomUUID();
//        Delivery delivery = new Delivery();
//        delivery.setId(deliveryId);
//        delivery.setStatus(DeliveryStatus.CREATED);
//        when(deliveryRepo.findById(deliveryId)).thenReturn(Optional.of(delivery));
//        deliveryService.deleteDelivery(deliveryId.toString());
//        verify(deliveryRepo, times(1)).deleteById(deliveryId);
//
//        // Also test DELIVERED status as allowed
//        delivery.setStatus(DeliveryStatus.DELIVERED);
//        when(deliveryRepo.findById(deliveryId)).thenReturn(Optional.of(delivery));
//        deliveryService.deleteDelivery(deliveryId.toString());
//        verify(deliveryRepo, times(2)).deleteById(deliveryId);
//    }
//
//    @Test
//    public void deleteDelivery_should_throw_when_status_not_allow() {
//        UUID deliveryId = UUID.randomUUID();
//        Delivery delivery = new Delivery();
//        delivery.setId(deliveryId);
//        delivery.setStatus(DeliveryStatus.IN_TRANSIT);
//
//
//        when(deliveryRepo.findById(deliveryId)).thenReturn(Optional.of(delivery));
//        assertThrows(RuntimeException.class, () -> deliveryService.deleteDelivery(deliveryId.toString()));
//    }
//
//    @Test
//    public void deleteDelivery_should_throw_when_not_found() {
//        UUID deliveryId = UUID.randomUUID();
//        when(deliveryRepo.findById(deliveryId)).thenReturn(Optional.empty());
//        assertThrows(ResourceNotFoundException.class, () -> deliveryService.deleteDelivery(deliveryId.toString()));
//    }
//}
