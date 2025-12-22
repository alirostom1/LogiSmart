package io.github.alirostom1.logismart.dto.response.delivery;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.alirostom1.logismart.model.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeliveryStatusResponse {
    private String id;
    private DeliveryStatus status;
    private LocalDateTime lastUpdate;
    private String currentLocation;
}