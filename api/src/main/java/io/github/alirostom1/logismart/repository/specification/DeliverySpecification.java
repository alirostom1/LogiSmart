package io.github.alirostom1.logismart.repository.specification;

import io.github.alirostom1.logismart.model.entity.Delivery;
import io.github.alirostom1.logismart.model.enums.DeliveryPriority;
import io.github.alirostom1.logismart.model.enums.DeliveryStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.UUID;

public class DeliverySpecification {

    public static Specification<Delivery> hasSearchTerm(String searchTerm) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(searchTerm)) return cb.conjunction();
            String pattern = "%" + searchTerm.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("description")), pattern),
                    cb.like(cb.lower(root.get("pickupName")), pattern),
                    cb.like(cb.lower(root.get("deliveryName")), pattern),
                    cb.like(cb.lower(root.get("pickupPhone")), pattern),
                    cb.like(cb.lower(root.get("deliveryPhone")), pattern),
                    cb.like(cb.lower(root.get("trackingNumber")), pattern)
            );
        };
    }

    public static Specification<Delivery> hasStatus(String status) {
        return (root, query, cb) -> StringUtils.hasText(status)
                ? cb.equal(root.get("status"), DeliveryStatus.valueOf(status))
                : cb.conjunction();
    }

    public static Specification<Delivery> hasPriority(String priority) {
        return (root, query, cb) -> StringUtils.hasText(priority)
                ? cb.equal(root.get("priority"), DeliveryPriority.valueOf(priority))
                : cb.conjunction();
    }

    public static Specification<Delivery> hasPickupZone(Long zoneId) {
        return (root, query, cb) -> zoneId != null
                ? cb.equal(root.get("pickupZone").get("id"), zoneId)
                : cb.conjunction();
    }

    public static Specification<Delivery> hasDeliveryZone(Long zoneId) {
        return (root, query, cb) -> zoneId != null
                ? cb.equal(root.get("deliveryZone").get("id"), zoneId)
                : cb.conjunction();
    }
}
