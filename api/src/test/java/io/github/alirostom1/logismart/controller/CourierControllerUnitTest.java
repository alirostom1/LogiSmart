//package io.github.alirostom1.logismart.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.github.alirostom1.logismart.dto.request.courier.CreateCourierRequest;
//import io.github.alirostom1.logismart.dto.request.courier.UpdateCourierRequest;
//import io.github.alirostom1.logismart.dto.response.courier.CourierResponse;
//import io.github.alirostom1.logismart.dto.response.courier.CourierWithDeliveriesResponse;
//import io.github.alirostom1.logismart.dto.response.zone.ZoneResponse;
//import io.github.alirostom1.logismart.service.CourierService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
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
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.hamcrest.Matchers.*;
//
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(CourierController.class)
//public class CourierControllerUnitTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private CourierService courierService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private final String baseUrl = "/api/v2/couriers";
//
//    @Test
//    void createCourier_shouldReturnCreated() throws Exception {
//        CreateCourierRequest request = new CreateCourierRequest("Doe", "John", "Bike", "0600000000", UUID.randomUUID().toString());
//        CourierResponse response = new CourierResponse(UUID.randomUUID().toString(), "Doe", "John", "Bike", "0600000000", null, "2025-11-13T15:22:01", null);
//        when(courierService.createCourier(any(CreateCourierRequest.class))).thenReturn(response);
//
//        mockMvc.perform(post(baseUrl)
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.data.lastName", is("Doe")));
//    }
//
//    @Test
//    void getCourierById_shouldReturnCourier() throws Exception {
//        String id = UUID.randomUUID().toString();
//        CourierResponse response = new CourierResponse(id, "Doe", "John", "Bike", "0600000000", null, "2025-11-13T15:22:01", null);
//        when(courierService.getCourierById(id)).thenReturn(response);
//
//        mockMvc.perform(get(baseUrl + "/" + id))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.id", is(id)));
//    }
//
//    @Test
//    void getCourierWithDeliveries_shouldReturnDeliveries() throws Exception {
//        String id = UUID.randomUUID().toString();
//        CourierWithDeliveriesResponse response = CourierWithDeliveriesResponse.builder()
//                .id(id)
//                .lastName("Doe")
//                .firstName("John")
//                .vehicle("Bike")
//                .phoneNumber("0600000000")
//                .zone(ZoneResponse.builder().id(UUID.randomUUID().toString()).name("Zone 1").postalCode(12345).build())
//                .collectingDeliveries(Collections.emptyList())
//                .shippingDeliveries(Collections.emptyList())
//                .totalDeliveries(0)
//                .pendingDeliveries(0)
//                .completedDeliveries(0)
//                .createdAt("2025-11-13T15:22:01")
//                .build();
//        when(courierService.getCourierWithDeliveries(id)).thenReturn(response);
//
//        mockMvc.perform(get(baseUrl + "/" + id + "/with-deliveries"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.id", is(id)));
//    }
//
//    @Test
//    void updateCourier_shouldReturnUpdated() throws Exception {
//        String id = UUID.randomUUID().toString();
//        UpdateCourierRequest request = new UpdateCourierRequest("Smith", "Jane", "Car", "0700000000", UUID.randomUUID().toString());
//        CourierResponse response = new CourierResponse(id, "Smith", "Jane", "Car", "0700000000", null, "2025-11-13T15:30:00", "2025-11-13T15:35:00");
//        when(courierService.updateCourier(anyString(), any(UpdateCourierRequest.class))).thenReturn(response);
//
//        mockMvc.perform(put(baseUrl + "/" + id)
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.firstName", is("Jane")));
//    }
//
//    @Test
//    void getAllCouriers_shouldReturnPage() throws Exception {
//        Pageable pageable = PageRequest.of(0, 10);
//        CourierResponse cr = new CourierResponse(UUID.randomUUID().toString(), "Carl", "Sue", "Truck", "0500000000", null, "2025-11-13T15:22:01", null);
//        Page<CourierResponse> page = new PageImpl<>(Collections.singletonList(cr), pageable, 1);
//        when(courierService.getAllCouriers(any(Pageable.class))).thenReturn(page);
//
//        mockMvc.perform(get(baseUrl)
//                        .param("page", "0")
//                        .param("size", "10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.content", hasSize(1)));
//    }
//
//    @Test
//    void getCouriersByZone_shouldReturnPage() throws Exception {
//        String zoneId = UUID.randomUUID().toString();
//        Pageable pageable = PageRequest.of(0, 10);
//        CourierResponse cr = new CourierResponse(UUID.randomUUID().toString(), "Alice", "Bob", "Scooter", "0800000000", null, "2025-11-13T15:22:01", null);
//        Page<CourierResponse> page = new PageImpl<>(Collections.singletonList(cr), pageable, 1);
//        when(courierService.getCouriersByZone(anyString(), any(Pageable.class))).thenReturn(page);
//
//        mockMvc.perform(get(baseUrl + "/zone/" + zoneId)
//                        .param("page", "0")
//                        .param("size", "10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.content", hasSize(1)));
//    }
//
//    @Test
//    void deleteCourier_shouldReturnOk() throws Exception {
//        String id = UUID.randomUUID().toString();
//        mockMvc.perform(delete(baseUrl + "/" + id))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message", containsString("deleted")));
//    }
//}
