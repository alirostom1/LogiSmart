package io.github.alirostom1.logismart.repository;

import io.github.alirostom1.logismart.model.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ZoneRepo extends JpaRepository<Zone, UUID>{
    boolean existsByPostalCode(int postalCode);
    boolean existsByPostalCodeAndIdNot(int postalCode,UUID id);
}
