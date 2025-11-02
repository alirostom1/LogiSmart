package io.github.alirostom1.logismart.dto.response.courier;

import io.github.alirostom1.logismart.dto.response.zone.ZoneResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourierResponse {
    private String id;
    private String lastName;
    private String firstName;
    private String vehicle;
    private String phoneNumber;
    private ZoneResponse zone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}