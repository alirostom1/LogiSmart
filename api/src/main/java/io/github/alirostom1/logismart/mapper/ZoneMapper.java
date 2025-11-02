package io.github.alirostom1.logismart.mapper;

import io.github.alirostom1.logismart.dto.request.zone.CreateZoneRequest;
import io.github.alirostom1.logismart.dto.response.zone.ZoneResponse;
import io.github.alirostom1.logismart.model.entity.Zone;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ZoneMapper {

    ZoneResponse toResponse(Zone zone);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "couriers", ignore = true)
    @Mapping(target = "deliveries", ignore = true)
    Zone toEntity(CreateZoneRequest request);
}