package io.github.alirostom1.logismart.service;

import io.github.alirostom1.logismart.dto.request.permission.CreatePermissionRequest;
import io.github.alirostom1.logismart.dto.response.permission.PermissionResponse;
import io.github.alirostom1.logismart.dto.response.role.RoleResponse;
import io.github.alirostom1.logismart.exception.PermissionAlreadyExistsException;
import io.github.alirostom1.logismart.exception.ResourceNotFoundException;
import io.github.alirostom1.logismart.model.entity.Permission;
import io.github.alirostom1.logismart.model.entity.Role;
import io.github.alirostom1.logismart.model.enums.EPermission;
import io.github.alirostom1.logismart.repository.PermissionRepo;
import io.github.alirostom1.logismart.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepo permissionRepo;
    private final RoleRepository roleRepository;

    public PermissionResponse createPermission(CreatePermissionRequest request) {
        if (permissionRepo.existsByName(request.getName())) {
            throw new PermissionAlreadyExistsException("Permission already exists: " + request.getName());
        }
        
        Permission permission = Permission.builder()
                .name(request.getName())
                .build();
        
        Permission saved = permissionRepo.save(permission);
        return toResponse(saved);
    }

    public void deletePermission(Long permissionId) {
        Permission permission = permissionRepo.findById(permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found"));
        
        // Remove permission from all roles
        List<Role> roles = roleRepository.findAll();
        roles.forEach(role -> role.removePermission(permission));
        roleRepository.saveAll(roles);
        
        permissionRepo.delete(permission);
    }

    public void assignPermissionToRole(Long roleId, Long permissionId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        Permission permission = permissionRepo.findById(permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found"));
        
        role.addPermission(permission);
        roleRepository.save(role);
    }

    public void revokePermissionFromRole(Long roleId, Long permissionId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        Permission permission = permissionRepo.findById(permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found"));
        
        role.removePermission(permission);
        roleRepository.save(role);
    }

    @Transactional(readOnly = true)
    public RoleResponse getRolePermissions(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .permissions(role.getPermissions().stream()
                        .map(this::toResponse)
                        .collect(Collectors.toSet()))
                .createdAt(role.getCreatedAt())
                .updatedAt(role.getUpdatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public List<PermissionResponse> getAllPermissions() {
        return permissionRepo.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private PermissionResponse toResponse(Permission permission) {
        return PermissionResponse.builder()
                .id(permission.getId())
                .name(permission.getName())
                .createdAt(permission.getCreatedAt())
                .updatedAt(permission.getUpdatedAt())
                .build();
    }
}
