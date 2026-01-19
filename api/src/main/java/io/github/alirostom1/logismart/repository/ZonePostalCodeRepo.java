package io.github.alirostom1.logismart.repository;

import io.github.alirostom1.logismart.model.entity.Zone;
import io.github.alirostom1.logismart.model.entity.ZonePostalCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ZonePostalCodeRepo extends JpaRepository<ZonePostalCode,Long> {
    Optional<ZonePostalCode> findZoneByPostalCode(String postalCode);
    boolean existsByPostalCode(String postalCode);
    List<ZonePostalCode> findByZoneIdAndPostalCodeIn(Long zoneId, List<String> postalCodes);
    
    @Modifying
    @Query("DELETE FROM ZonePostalCode zpc WHERE zpc.zone.id = :zoneId AND zpc.postalCode IN :postalCodes")
    void deleteByZoneIdAndPostalCodeIn(@Param("zoneId") Long zoneId, @Param("postalCodes") List<String> postalCodes);
}
