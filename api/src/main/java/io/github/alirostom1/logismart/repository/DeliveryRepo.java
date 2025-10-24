package io.github.alirostom1.logismart.repository;


import io.github.alirostom1.logismart.model.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryRepo extends JpaRepository<Delivery, UUID> {
}
