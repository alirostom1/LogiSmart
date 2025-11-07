package io.github.alirostom1.logismart.mapper;

import io.github.alirostom1.logismart.dto.response.courier.CourierResponse;
import io.github.alirostom1.logismart.model.entity.Courier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SimpleCourierMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "firstName", source = "firstName")
    CourierResponse toSimpleResponse(Courier courier);
}

