package io.github.alirostom1.logismart.controller;

import io.github.alirostom1.logismart.dto.request.zone.CreateZoneRequest;
import io.github.alirostom1.logismart.dto.response.zone.ZoneResponse;
import io.github.alirostom1.logismart.service.ZoneService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ZoneController.class)
public class ZoneControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ZoneService zoneService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void index_should_return_ok() throws Exception {
        Pageable pageable = PageRequest.of(0,10);
        Page<ZoneResponse> page = new PageImpl<>(
                List.of(
                        new ZoneResponse(UUID.randomUUID().toString(),"zone1",12737123),
                        new ZoneResponse(UUID.randomUUID().toString(),"zone2",1273213641)
                ),
                pageable,
                2
        );
        when(zoneService.getAllZones(pageable)).thenReturn(page);

        mockMvc.perform(get("/api/v2/zones")
                        .param("page","0")
                        .param("size","10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content",hasSize(2)));
    }

    @Test
    public void show_should_return_zone() throws Exception {
        String id = UUID.randomUUID().toString();
        ZoneResponse zone = new ZoneResponse(id, "zone1", 111);
        when(zoneService.getZoneById(id)).thenReturn(zone);

        mockMvc.perform(get("/api/v2/zones/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(id)));
    }

    @Test
    public void store_should_create_zone() throws Exception {
        CreateZoneRequest request = new CreateZoneRequest("name",9123);
        // Set properties on request as needed
        ZoneResponse response = new ZoneResponse(UUID.randomUUID().toString(), "zoneNew", 111);

        when(zoneService.createZone(any(CreateZoneRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v2/zones")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name", is("zoneNew")));
    }

    @Test
    public void update_should_update_zone() throws Exception {
        String id = UUID.randomUUID().toString();
        CreateZoneRequest request = new CreateZoneRequest("name",9123);
        // Set properties on request as needed
        ZoneResponse response = new ZoneResponse(id, "zoneUpdated", 123);

        when(zoneService.updateZone(any(String.class), any(CreateZoneRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v2/zones/{id}", id)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name", is("zoneUpdated")));
    }

    @Test
    public void delete_should_delete_zone() throws Exception {
        String id = UUID.randomUUID().toString();
        mockMvc.perform(delete("/api/v2/zones/{id}", id))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", containsString("deleted")));
    }
}
