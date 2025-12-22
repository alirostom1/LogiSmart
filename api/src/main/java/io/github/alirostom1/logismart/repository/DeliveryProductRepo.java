package io.github.alirostom1.logismart.repository;

import io.github.alirostom1.logismart.model.entity.DeliveryProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryProductRepo extends JpaRepository<DeliveryProduct, Long> {
}
