//package io.github.alirostom1.logismart.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.github.alirostom1.logismart.dto.request.delivery.CreateDeliveryRequest;
//import io.github.alirostom1.logismart.dto.request.delivery.UpdateDeliveryStatusRequest;
//import io.github.alirostom1.logismart.model.entity.*;
//import io.github.alirostom1.logismart.repository.DeliveryRepo;
//import io.github.alirostom1.logismart.repository.PersonRepo;
//import io.github.alirostom1.logismart.repository.ZoneRepo;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@Transactional
//public class DeliveryControllerIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private DeliveryRepo deliveryRepository;
//    @Autowired
//    private ZoneRepo zoneRepo;
//    @Autowired
//    private PersonRepo personRepo;
//
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private UUID deliveryId;
//    private UUID zoneId;
//    private UUID recipientId;
//    private UUID senderId;
//
//    @BeforeEach
//    void setUp() {
//        deliveryRepository.deleteAll();
//        zoneRepo.deleteAll();
//        personRepo.deleteAll();
//
//        Zone zone = new Zone();
//        zone.setName("TestZone");
//        zone.setPostalCode(75000);
//        UUID zoneId = zoneRepo.save(zone).getId();
//
//        Sender sender = new Sender();
//        sender.setFirstName("Alice");
//        sender.setLastName("Smith");
//        sender.setEmail("alice@example.com");
//        sender.setPhone("0123456789");
//        sender.setAddress("123 Main St");
//        UUID senderId = personRepo.save(sender).getId();
//
//        Recipient recipient = new Recipient();
//        recipient.setFirstName("Bob");
//        recipient.setLastName("White");
//        recipient.setEmail("bob@example.com");
//        recipient.setPhone("0987654321");
//        recipient.setAddress("456 Side St");
//        UUID recipientId = personRepo.save(recipient).getId();
//
//
//        Delivery delivery = new Delivery();
//        delivery.setRecipient(recipient);
//        delivery.setSender(sender);
//        delivery.setZone(zone);
//        delivery.setDescription("Integration Test Delivery");
//        delivery.setDestinationCity("safi");
//
//        this.deliveryId = deliveryRepository.saveAndFlush(delivery).getId();
//        this.senderId = senderId;
//        this.recipientId = recipientId;
//        this.zoneId = zoneId;
//    }
//
//    @Test
//    void createDelivery_persistsToDatabase_andReturnsResponse() throws Exception {
//        CreateDeliveryRequest request = new CreateDeliveryRequest(
//                "A box to Paris",
//                "Paris",
//                2.5,
//                "HIGH",
//                senderId.toString(),
//                recipientId.toString(),
//                zoneId.toString(),
//                List.of()
//        );
//
//        mockMvc.perform(post("/api/v2/deliveries")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.success").value(true));
//
//        assertThat(deliveryRepository.findAll()).isNotEmpty();
//    }
//
//    @Test
//    void getDeliveryById_readsFromDatabase_andReturnsResponse() throws Exception {
//        mockMvc.perform(get("/api/v2/deliveries/" + deliveryId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data.id").value(deliveryId.toString()));
//    }
//
//    @Test
//    void updateDeliveryStatus_modifiesDatabase_andReturnsResponse() throws Exception {
//        UpdateDeliveryStatusRequest request = new UpdateDeliveryStatusRequest("DELIVERED", "Received by client");
//
//        mockMvc.perform(patch("/api/v2/deliveries/" + deliveryId + "/status")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true));
//    }
//
//    @Test
//    void deleteDelivery_removesFromDatabase_andReturnsOk() throws Exception {
//        mockMvc.perform(delete("/api/v2/deliveries/" + deliveryId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").exists());
//        assertThat(deliveryRepository.existsById(deliveryId)).isFalse();
//    }
//}
