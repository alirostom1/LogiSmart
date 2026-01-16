package io.github.alirostom1.logismart.repository;

import io.github.alirostom1.logismart.model.entity.Zone;
import io.github.alirostom1.logismart.model.entity.ZonePostalCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ZonePostalCodeRepo extends JpaRepository<ZonePostalCode,Long> {
    Optional<ZonePostalCode> findZoneByPostalCode(String postalCode);
}
