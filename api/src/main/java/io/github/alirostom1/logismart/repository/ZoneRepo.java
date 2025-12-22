package io.github.alirostom1.logismart.repository;

import io.github.alirostom1.logismart.model.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ZoneRepo extends JpaRepository<Zone, Long>{
    Optional<Zone> findByName(String name);
    Optional<Zone> findByCode(String code);
}
