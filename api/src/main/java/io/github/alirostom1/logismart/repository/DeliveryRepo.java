package io.github.alirostom1.logismart.repository;


import io.github.alirostom1.logismart.model.entity.Delivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface DeliveryRepo extends JpaRepository<Delivery, UUID> , JpaSpecificationExecutor<Delivery> {
    Page<Delivery> findBySenderId(UUID senderId, Pageable pageable);
    Page<Delivery> findByRecipientId(UUID recipientId, Pageable pageable);
    Page<Delivery> findByCollectingCourierIdOrShippingCourierId(UUID collectId,UUID shippingId, Pageable pageable);
}
