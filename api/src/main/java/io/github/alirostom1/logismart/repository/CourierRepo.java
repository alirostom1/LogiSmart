package io.github.alirostom1.logismart.repository;

import io.github.alirostom1.logismart.model.entity.Courier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CourierRepo extends JpaRepository<Courier, UUID> {
    public boolean existsByPhoneNumber(String phone);
    public boolean existsByPhoneNumberAndIdNot(String phone,UUID id);
}
