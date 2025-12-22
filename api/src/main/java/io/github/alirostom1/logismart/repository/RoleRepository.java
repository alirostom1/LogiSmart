package io.github.alirostom1.logismart.repository;

import io.github.alirostom1.logismart.model.entity.Role;
import io.github.alirostom1.logismart.model.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(ERole role);
    boolean existsByName(ERole role);
}
