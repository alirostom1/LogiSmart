package io.github.alirostom1.logismart.repository;


import io.github.alirostom1.logismart.model.entity.Delivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface DeliveryRepo extends JpaRepository<Delivery, Long> , JpaSpecificationExecutor<Delivery> {
    Page<Delivery> findBySenderId(Long senderId, Pageable pageable);
    Page<Delivery> findByRecipientId(Long recipientId, Pageable pageable);
    Page<Delivery> findByCollectingCourierIdOrShippingCourierId(Long collectId,Long shippingId, Pageable pageable);

    Page<Delivery> findByRecipientPhone(String phone,Pageable pageable);
    Optional<Delivery> findByTrackingNumber(String trackingNumber);
}
