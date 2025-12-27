//package io.github.alirostom1.logismart.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.github.alirostom1.logismart.dto.request.delivery.AssignDeliveryRequest;
//import io.github.alirostom1.logismart.dto.request.delivery.CreateDeliveryRequest;
//import io.github.alirostom1.logismart.dto.request.delivery.SearchDeliveryRequest;
//import io.github.alirostom1.logismart.dto.request.delivery.UpdateDeliveryStatusRequest;
//import io.github.alirostom1.logismart.dto.response.delivery.DeliveryDetailsResponse;
//import io.github.alirostom1.logismart.dto.response.delivery.DeliveryResponse;
//import io.github.alirostom1.logismart.dto.response.delivery.DeliveryTrackingResponse;
//import io.github.alirostom1.logismart.service.DeliveryService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentMatchers;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Collections;
//import java.util.UUID;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.hamcrest.Matchers.*;
//
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(DeliveryController.class)
//public class DeliveryControllerUnitTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private DeliveryService deliveryService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private final String baseUrl = "/api/v2/deliveries";
//
//    @Test
//    void createDelivery_shouldReturnCreated() throws Exception {
//        CreateDeliveryRequest req = new CreateDeliveryRequest("desc", "Paris", 1.0, "HIGH", UUID.randomUUID().toString(),
//                UUID.randomUUID().toString(), UUID.randomUUID().toString(), Collections.emptyList());
//        DeliveryDetailsResponse resp = DeliveryDetailsResponse.builder().id(UUID.randomUUID().toString()).description("desc").build();
//        when(deliveryService.createDelivery(ArgumentMatchers.any(CreateDeliveryRequest.class))).thenReturn(resp);
//
//        mockMvc.perform(post(baseUrl)
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(req)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.data.description", is("desc")));
//    }
//
//    @Test
//    void getDeliveryById_shouldReturnDelivery() throws Exception {
//        String id = UUID.randomUUID().toString();
//        DeliveryDetailsResponse resp = DeliveryDetailsResponse.builder().id(id).description("delivery").build();
//        when(deliveryService.getDeliveryById(id)).thenReturn(resp);
//
//        mockMvc.perform(get(baseUrl + "/" + id))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.id", is(id)));
//    }
//
//    @Test
//    void trackDelivery_shouldReturnTracking() throws Exception {
//        String id = UUID.randomUUID().toString();
//        DeliveryTrackingResponse resp = new DeliveryTrackingResponse(id, "desc", null, "Paris", "Recipient", "Sender", "Coll", "Ship", "now");
//        when(deliveryService.getDeliveryTracking(id)).thenReturn(resp);
//
//        mockMvc.perform(get(baseUrl + "/" + id + "/tracking"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.id", is(id)));
//    }
//
//    @Test
//    void updateDeliveryStatus_shouldReturnUpdated() throws Exception {
//        String id = UUID.randomUUID().toString();
//        UpdateDeliveryStatusRequest req = new UpdateDeliveryStatusRequest("DELIVERED", "all good");
//        DeliveryDetailsResponse resp = DeliveryDetailsResponse.builder().id(id).status("DELIVERED").build();
//        when(deliveryService.updateDeliveryStatus(eq(id), ArgumentMatchers.any(UpdateDeliveryStatusRequest.class))).thenReturn(resp);
//
//        mockMvc.perform(patch(baseUrl + "/" + id + "/status")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(req)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.status", is("DELIVERED")));
//    }
//
//    @Test
//    void assignCollectingCourier_shouldAssign() throws Exception {
//        String id = UUID.randomUUID().toString();
//        AssignDeliveryRequest req = new AssignDeliveryRequest(UUID.randomUUID().toString());
//        DeliveryDetailsResponse resp = DeliveryDetailsResponse.builder().id(id).build();
//        when(deliveryService.assignCollectingCourier(eq(id), ArgumentMatchers.any(AssignDeliveryRequest.class))).thenReturn(resp);
//
//        mockMvc.perform(patch(baseUrl + "/" + id + "/assign-collecting-courier")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(req)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.id", is(id)));
//    }
//
//    @Test
//    void assignShippingCourier_shouldAssign() throws Exception {
//        String id = UUID.randomUUID().toString();
//        AssignDeliveryRequest req = new AssignDeliveryRequest(UUID.randomUUID().toString());
//        DeliveryDetailsResponse resp = DeliveryDetailsResponse.builder().id(id).build();
//        when(deliveryService.assignShippingCourier(eq(id), ArgumentMatchers.any(AssignDeliveryRequest.class))).thenReturn(resp);
//
//        mockMvc.perform(patch(baseUrl + "/" + id + "/assign-shipping-courier")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(req)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.id", is(id)));
//    }
//
//    @Test
//    void searchDeliveries_shouldReturnPage() throws Exception {
//        Pageable pageable = PageRequest.of(0, 10);
//        SearchDeliveryRequest req = new SearchDeliveryRequest();
//        DeliveryResponse dr = new DeliveryResponse(UUID.randomUUID().toString(), "desc", "Paris", 1.0, null, null, "rec", "send", "zone", null, null, "now", "now");
//        Page<DeliveryResponse> page = new PageImpl<>(Collections.singletonList(dr), pageable, 1);
//        when(deliveryService.searchDeliveries(ArgumentMatchers.any(SearchDeliveryRequest.class), ArgumentMatchers.any(Pageable.class))).thenReturn(page);
//
//        mockMvc.perform(get(baseUrl + "/search")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(req))
//                        .param("page", "0")
//                        .param("size", "10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.content", hasSize(1)));
//    }
//
//    @Test
//    void getDeliveriesBySender_shouldReturnPage() throws Exception {
//        Pageable pageable = PageRequest.of(0, 10);
//        String senderId = UUID.randomUUID().toString();
//        DeliveryResponse dr = new DeliveryResponse(UUID.randomUUID().toString(), "desc", "Paris", 1.0, null, null, "rec", "send", "zone", null, null, "now", "now");
//        Page<DeliveryResponse> page = new PageImpl<>(Collections.singletonList(dr), pageable, 1);
//        when(deliveryService.getDeliveriesBySender(eq(senderId), ArgumentMatchers.any(Pageable.class))).thenReturn(page);
//
//        mockMvc.perform(get(baseUrl + "/sender/" + senderId)
//                        .param("page", "0")
//                        .param("size", "10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.content", hasSize(1)));
//    }
//
//    @Test
//    void getDeliveriesByRecipient_shouldReturnPage() throws Exception {
//        Pageable pageable = PageRequest.of(0, 10);
//        String recipientId = UUID.randomUUID().toString();
//        DeliveryResponse dr = new DeliveryResponse(UUID.randomUUID().toString(), "desc", "Paris", 1.0, null, null, "rec", "send", "zone", null, null, "now", "now");
//        Page<DeliveryResponse> page = new PageImpl<>(Collections.singletonList(dr), pageable, 1);
//        when(deliveryService.getDeliveriesByRecipient(eq(recipientId), ArgumentMatchers.any(Pageable.class))).thenReturn(page);
//
//        mockMvc.perform(get(baseUrl + "/recipient/" + recipientId)
//                        .param("page", "0")
//                        .param("size", "10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.content", hasSize(1)));
//    }
//
//    @Test
//    void getDeliveriesByCourier_shouldReturnPage() throws Exception {
//        Pageable pageable = PageRequest.of(0, 10);
//        String courierId = UUID.randomUUID().toString();
//        DeliveryResponse dr = new DeliveryResponse(UUID.randomUUID().toString(), "desc", "Paris", 1.0, null, null, "rec", "send", "zone", null, null, "now", "now");
//        Page<DeliveryResponse> page = new PageImpl<>(Collections.singletonList(dr), pageable, 1);
//        when(deliveryService.getDeliveriesByCourier(eq(courierId), ArgumentMatchers.any(Pageable.class))).thenReturn(page);
//
//        mockMvc.perform(get(baseUrl + "/courier/" + courierId)
//                        .param("page", "0")
//                        .param("size", "10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.content", hasSize(1)));
//    }
//
//    @Test
//    void deleteDelivery_shouldDelete() throws Exception {
//        String id = UUID.randomUUID().toString();
//        doNothing().when(deliveryService).deleteDelivery(id);
//
//        mockMvc.perform(delete(baseUrl + "/" + id))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message", containsString("deleted")));
//    }
//}
