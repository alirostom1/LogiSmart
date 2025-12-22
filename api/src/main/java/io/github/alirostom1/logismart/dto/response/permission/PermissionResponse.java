package io.github.alirostom1.logismart.dto.response.permission;

import io.github.alirostom1.logismart.model.enums.EPermission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionResponse {
    private Long id;
    private EPermission name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

