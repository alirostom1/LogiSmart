package io.github.alirostom1.logismart.repository;

import io.github.alirostom1.logismart.model.entity.Courier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CourierRepo extends JpaRepository<Courier, UUID> {
    boolean existsByPhoneNumber(String phone);
    boolean existsByPhoneNumberAndIdNot(String phone,UUID id);
    Page<Courier> findByZoneId(UUID zoneId, Pageable pageable);
}
