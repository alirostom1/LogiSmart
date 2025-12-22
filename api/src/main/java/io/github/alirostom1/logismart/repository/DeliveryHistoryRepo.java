package io.github.alirostom1.logismart.repository;

import io.github.alirostom1.logismart.model.entity.DeliveryHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryHistoryRepo extends JpaRepository<DeliveryHistory, Long> {
}