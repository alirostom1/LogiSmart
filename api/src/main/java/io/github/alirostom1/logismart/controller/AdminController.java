package io.github.alirostom1.logismart.controller;

import io.github.alirostom1.logismart.dto.request.permission.AssignPermissionToRoleRequest;
import io.github.alirostom1.logismart.dto.request.permission.CreatePermissionRequest;
import io.github.alirostom1.logismart.dto.response.common.DefaultApiResponse;
import io.github.alirostom1.logismart.dto.response.permission.PermissionResponse;
import io.github.alirostom1.logismart.dto.response.role.RoleResponse;
import io.github.alirostom1.logismart.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v3/admin")
@Tag(name = "Admin API", description = "Administrative operations for permissions and roles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final PermissionService permissionService;

    @Operation(summary = "Create a new permission")
    @ApiResponse(responseCode = "201", description = "Permission created successfully")
    @PostMapping("/permissions")
    public ResponseEntity<DefaultApiResponse<PermissionResponse>> createPermission(
            @Valid @RequestBody CreatePermissionRequest request) {
        PermissionResponse response = permissionService.createPermission(request);
        DefaultApiResponse<PermissionResponse> apiResponse = new DefaultApiResponse<>(
                true,
                "Permission created successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @Operation(summary = "Delete a permission")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Permission deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Permission not found")
    })
    @DeleteMapping("/permissions/{permissionId}")
    public ResponseEntity<DefaultApiResponse<Void>> deletePermission(
            @Parameter(description = "Permission ID") @PathVariable Long permissionId) {
        permissionService.deletePermission(permissionId);
        DefaultApiResponse<Void> apiResponse = new DefaultApiResponse<>(
                true,
                "Permission deleted successfully!",
                null,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Get all permissions")
    @ApiResponse(responseCode = "200", description = "Permissions retrieved successfully")
    @GetMapping("/permissions")
    public ResponseEntity<DefaultApiResponse<List<PermissionResponse>>> getAllPermissions() {
        List<PermissionResponse> response = permissionService.getAllPermissions();
        DefaultApiResponse<List<PermissionResponse>> apiResponse = new DefaultApiResponse<>(
                true,
                "Permissions retrieved successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Assign a permission to a role")
    @ApiResponse(responseCode = "200", description = "Permission assigned successfully")
    @PostMapping("/roles/assign-permission")
    public ResponseEntity<DefaultApiResponse<Void>> assignPermissionToRole(
            @Valid @RequestBody AssignPermissionToRoleRequest request) {
        permissionService.assignPermissionToRole(request.getRoleId(), request.getPermissionId());
        DefaultApiResponse<Void> apiResponse = new DefaultApiResponse<>(
                true,
                "Permission assigned to role successfully!",
                null,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Revoke a permission from a role")
    @ApiResponse(responseCode = "200", description = "Permission revoked successfully")
    @PostMapping("/roles/revoke-permission")
    public ResponseEntity<DefaultApiResponse<Void>> revokePermissionFromRole(
            @Valid @RequestBody AssignPermissionToRoleRequest request) {
        permissionService.revokePermissionFromRole(request.getRoleId(), request.getPermissionId());
        DefaultApiResponse<Void> apiResponse = new DefaultApiResponse<>(
                true,
                "Permission revoked from role successfully!",
                null,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Get all permissions for a role")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Role permissions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    @GetMapping("/roles/{roleId}/permissions")
    public ResponseEntity<DefaultApiResponse<RoleResponse>> getRolePermissions(
            @Parameter(description = "Role ID") @PathVariable Long roleId) {
        RoleResponse response = permissionService.getRolePermissions(roleId);
        DefaultApiResponse<RoleResponse> apiResponse = new DefaultApiResponse<>(
                true,
                "Role permissions retrieved successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(apiResponse);
    }
}

