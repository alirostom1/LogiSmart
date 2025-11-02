package io.github.alirostom1.logismart.dto.response.client;

import io.github.alirostom1.logismart.dto.response.common.PersonResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class SenderResponse extends PersonResponse {
    private int totalDeliveriesSent;
    private int activeDeliveries;
}
