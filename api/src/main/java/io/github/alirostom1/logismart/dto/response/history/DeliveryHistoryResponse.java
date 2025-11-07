package io.github.alirostom1.logismart.dto.response.history;

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
public class DeliveryHistoryResponse {
    private String id;
    private DeliveryStatus status;
    private String updatedAt;
    private String comment;
}