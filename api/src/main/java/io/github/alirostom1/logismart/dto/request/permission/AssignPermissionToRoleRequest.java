package io.github.alirostom1.logismart.dto.request.permission;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignPermissionToRoleRequest {
    @NotNull(message = "Role ID is required")
    private Long roleId;
    
    @NotNull(message = "Permission ID is required")
    private Long permissionId;
}

