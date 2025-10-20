package io.github.alirostom1.smartlogi.repository;


import io.github.alirostom1.smartlogi.model.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryRepo extends JpaRepository<Delivery, UUID> {
}
