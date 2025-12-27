//package io.github.alirostom1.logismart.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.github.alirostom1.logismart.dto.request.person.CreatePersonRequest;
//import io.github.alirostom1.logismart.dto.request.person.UpdatePersonRequest;
//import io.github.alirostom1.logismart.dto.response.client.RecipientResponse;
//import io.github.alirostom1.logismart.dto.response.client.SenderResponse;
//import io.github.alirostom1.logismart.service.PersonService;
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
//@WebMvcTest(PersonController.class)
//public class PersonControllerUnitTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private PersonService personService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private final String baseUrl = "/api/v2/persons";
//
//    // --- SENDERS ---
//
//    @Test
//    void showAllSenders_shouldReturnAllSenders() throws Exception {
//        Pageable pageable = PageRequest.of(0, 10);
//        SenderResponse resp = new SenderResponse();
//        resp.setLastName("Smith");
//        resp.setFirstName("Adam");
//        Page<SenderResponse> page = new PageImpl<>(Collections.singletonList(resp), pageable, 1);
//        when(personService.getAllSenders(ArgumentMatchers.any(Pageable.class))).thenReturn(page);
//
//        mockMvc.perform(get(baseUrl + "/senders")
//                        .param("page", "0")
//                        .param("size", "10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.content", hasSize(1)));
//    }
//
//    @Test
//    void showSender_shouldReturnSenderById() throws Exception {
//        String id = UUID.randomUUID().toString();
//        SenderResponse resp = new SenderResponse();
//        resp.setId(id);
//        when(personService.getSenderById(id)).thenReturn(resp);
//
//        mockMvc.perform(get(baseUrl + "/sender/" + id))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.id", is(id)));
//    }
//
//    @Test
//    void createSender_shouldCreateSender() throws Exception {
//        CreatePersonRequest req = new CreatePersonRequest("Smith", "Adam", "adam@email.com", "1234567890", "123 St");
//        SenderResponse resp = new SenderResponse();
//        resp.setFirstName("Adam");
//        when(personService.createSender(ArgumentMatchers.any(CreatePersonRequest.class))).thenReturn(resp);
//
//        mockMvc.perform(post(baseUrl + "/sender")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(req)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.firstName", is("Adam")));
//    }
//
//    @Test
//    void updateSender_shouldUpdateSender() throws Exception {
//        String id = UUID.randomUUID().toString();
//        UpdatePersonRequest req = new UpdatePersonRequest("Mayer", "Sam", "sam@email.com", "1231231234", "789 St");
//        SenderResponse resp = new SenderResponse();
//        resp.setId(id);
//        resp.setFirstName("Sam");
//        when(personService.updateSender(eq(id), ArgumentMatchers.any(UpdatePersonRequest.class))).thenReturn(resp);
//
//        mockMvc.perform(put(baseUrl + "/sender/" + id)
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(req)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.firstName", is("Sam")));
//    }
//
//    @Test
//    void deleteSender_shouldDeleteSender() throws Exception {
//        String id = UUID.randomUUID().toString();
//        doNothing().when(personService).deleteSender(id);
//
//        mockMvc.perform(delete(baseUrl + "/sender/" + id))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message", containsString("deleted")));
//    }
//
//    // --- RECIPIENTS ---
//
//    @Test
//    void showAllRecipients_shouldReturnAllRecipients() throws Exception {
//        Pageable pageable = PageRequest.of(0, 10);
//        RecipientResponse resp = new RecipientResponse();
//        resp.setLastName("Jones");
//        resp.setFirstName("Kate");
//        Page<RecipientResponse> page = new PageImpl<>(Collections.singletonList(resp), pageable, 1);
//        when(personService.getAllRecipients(ArgumentMatchers.any(Pageable.class))).thenReturn(page);
//
//        mockMvc.perform(get(baseUrl + "/recipients")
//                        .param("page", "0")
//                        .param("size", "10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.content", hasSize(1)));
//    }
//
//    @Test
//    void showRecipient_shouldReturnRecipientById() throws Exception {
//        String id = UUID.randomUUID().toString();
//        RecipientResponse resp = new RecipientResponse();
//        resp.setId(id);
//        when(personService.getRecipientById(id)).thenReturn(resp);
//
//        mockMvc.perform(get(baseUrl + "/recipient/" + id))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.id", is(id)));
//    }
//
//    @Test
//    void createRecipient_shouldCreateRecipient() throws Exception {
//        CreatePersonRequest req = new CreatePersonRequest("Jones", "Kate", "kate@email.com", "0987654321", "456 Ave");
//        RecipientResponse resp = new RecipientResponse();
//        resp.setFirstName("Kate");
//        when(personService.createRecipient(ArgumentMatchers.any(CreatePersonRequest.class))).thenReturn(resp);
//
//        mockMvc.perform(post(baseUrl + "/recipient")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(req)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.firstName", is("Kate")));
//    }
//
//    @Test
//    void updateRecipient_shouldUpdateRecipient() throws Exception {
//        String id = UUID.randomUUID().toString();
//        UpdatePersonRequest req = new UpdatePersonRequest("Lopez", "Zara", "zara@email.com", "2223334444", "222 Pl");
//        RecipientResponse resp = new RecipientResponse();
//        resp.setId(id);
//        resp.setFirstName("Zara");
//        when(personService.updateRecipient(eq(id), ArgumentMatchers.any(UpdatePersonRequest.class))).thenReturn(resp);
//
//        mockMvc.perform(put(baseUrl + "/recipient/" + id)
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(req)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.data.firstName", is("Zara")));
//    }
//
//    @Test
//    void deleteRecipient_shouldDeleteRecipient() throws Exception {
//        String id = UUID.randomUUID().toString();
//        doNothing().when(personService).deleteSender(id);
//
//        mockMvc.perform(delete(baseUrl + "/recipient/" + id))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message", containsString("deleted")));
//    }
//}
