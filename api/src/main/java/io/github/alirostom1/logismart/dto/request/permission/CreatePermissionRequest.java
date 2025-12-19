package io.github.alirostom1.logismart.dto.request.permission;

import io.github.alirostom1.logismart.model.enums.EPermission;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePermissionRequest {
    @NotNull(message = "Permission name is required")
    private EPermission name;
}
