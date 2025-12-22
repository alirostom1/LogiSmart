package io.github.alirostom1.logismart.mapper;

import io.github.alirostom1.logismart.dto.request.zone.CreateZoneRequest;
import io.github.alirostom1.logismart.dto.response.zone.ZoneResponse;
import io.github.alirostom1.logismart.model.entity.Zone;
import io.github.alirostom1.logismart.model.entity.ZonePostalCode;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ZoneMapper {

    @Mapping(target = "totalPostalCodes", expression = "java(zone.getPostalCodes() != null ? zone.getPostalCodes().size() : 0)")
    @Mapping(target = "postalCodes",source = "postalCodes",qualifiedByName = "postalCodeList")
    ZoneResponse toResponse(Zone zone);

    @Named("postalCodeList")
    default List<String> mapPostalCodes(Set<ZonePostalCode> postalCodes) {
        if (postalCodes == null) return List.of();
        return postalCodes.stream()
                .map(ZonePostalCode::getPostalCode)
                .sorted()
                .toList();
    }
}