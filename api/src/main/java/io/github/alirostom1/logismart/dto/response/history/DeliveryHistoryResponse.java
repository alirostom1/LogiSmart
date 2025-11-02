package io.github.alirostom1.logismart.dto.response.history;

import io.github.alirostom1.logismart.model.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryHistoryResponse {
    private String id;
    private DeliveryStatus status;
    private LocalDateTime updatedAt;
    private String comment;
}