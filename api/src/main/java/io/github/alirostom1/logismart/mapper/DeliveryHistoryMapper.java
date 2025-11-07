// src/main/java/io/github/alirostom1/logismart/mapper/DeliveryHistoryMapper.java
package io.github.alirostom1.logismart.mapper;

import io.github.alirostom1.logismart.dto.response.history.DeliveryHistoryResponse;
import io.github.alirostom1.logismart.model.entity.DeliveryHistory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeliveryHistoryMapper {

    DeliveryHistoryResponse toResponse(DeliveryHistory deliveryHistory);

    List<DeliveryHistoryResponse> toResponseList(List<DeliveryHistory> deliveryHistoryList);
}