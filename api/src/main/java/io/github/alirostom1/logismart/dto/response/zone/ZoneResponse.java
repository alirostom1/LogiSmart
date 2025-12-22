package io.github.alirostom1.logismart.dto.response.zone;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public record ZoneResponse(
        Long id,
        String name,
        String code,
        boolean active,
        int totalPostalCodes,
        List<String> postalCodes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}