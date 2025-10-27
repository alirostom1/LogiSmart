package io.github.alirostom1.logismart.repository;


import io.github.alirostom1.logismart.model.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface DeliveryRepo extends JpaRepository<Delivery, UUID> {
}
