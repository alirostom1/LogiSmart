package io.github.alirostom1.logismart.mapper;

import io.github.alirostom1.logismart.dto.request.person.RegisterRequest;
import io.github.alirostom1.logismart.dto.response.client.SenderResponse;
import io.github.alirostom1.logismart.model.entity.Sender;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SenderMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password",ignore = true)
    @Mapping(target = "deliveries", ignore = true)
    Sender toSenderEntity(RegisterRequest request);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "totalDeliveriesSent" ,ignore = true)
    @Mapping(target = "activeDeliveries",ignore = true)
    SenderResponse entitytoRegisterResponse(Sender sender);
}
