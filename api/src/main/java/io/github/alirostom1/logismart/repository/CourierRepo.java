package io.github.alirostom1.logismart.repository;

import io.github.alirostom1.logismart.model.entity.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CourierRepo extends JpaRepository<Courier, UUID> {
    public boolean existsByPhoneNumber(String phone);
    public boolean existsByPhoneNumberAndIdNot(String phone,UUID id);
}
