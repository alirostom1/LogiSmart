package io.github.alirostom1.logismart.repository;

import io.github.alirostom1.logismart.model.entity.Permission;
import io.github.alirostom1.logismart.model.enums.EPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepo extends JpaRepository<Permission,Long>{
    boolean existsByName(EPermission name);
}
