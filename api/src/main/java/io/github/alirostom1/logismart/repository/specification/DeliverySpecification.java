package io.github.alirostom1.logismart.repository.specification;

import io.github.alirostom1.logismart.model.entity.Delivery;
import io.github.alirostom1.logismart.model.enums.DeliveryPriority;
import io.github.alirostom1.logismart.model.enums.DeliveryStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.UUID;

public class DeliverySpecification {
    public static Specification<Delivery> hasSearchTerm(String searchTerm){
        return (root, query, criteriaBuilder) -> {
          if(!StringUtils.hasText(searchTerm)){
              return criteriaBuilder.conjunction();
          }
          String pattern = "%" + searchTerm.toLowerCase() + "%";
          return criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),pattern),
                  criteriaBuilder.like(criteriaBuilder.lower(root.get("destinationCity")),pattern)
          );
        };
    }
    public static Specification<Delivery> hasStatus(String status){
        return (root, query, criteriaBuilder) ->{
            if(!StringUtils.hasText(status)){
                return criteriaBuilder.conjunction();
            }
            try{
                DeliveryStatus statusEnum = DeliveryStatus.valueOf(status);
                return criteriaBuilder.equal(root.get("status"),statusEnum);
            }catch (IllegalArgumentException e){
                return criteriaBuilder.conjunction();
            }
        };
    }
    public static Specification<Delivery> hasPriority(String priority){
        return (root, query, criteriaBuilder) -> {
          if(!StringUtils.hasText(priority)){
              return criteriaBuilder.conjunction();
          }
          try{
              DeliveryPriority priorityEnum = DeliveryPriority.valueOf(priority);
              return criteriaBuilder.equal(root.get("priority"),priorityEnum);
          }catch(IllegalArgumentException e){
              return criteriaBuilder.conjunction();
          }
        };
    }
    public static Specification<Delivery> hasZoneId(String zoneId) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(zoneId)) {
                return criteriaBuilder.conjunction();
            }
            try {
                UUID zoneUuid = UUID.fromString(zoneId);
                return criteriaBuilder.equal(root.get("zone").get("id"), zoneUuid);
            } catch (IllegalArgumentException e) {
                return criteriaBuilder.conjunction();
            }
        };
    }
    public static Specification<Delivery> hasCity(String city) {
        return (root, query, criteriaBuilder) ->
                StringUtils.hasText(city) ? criteriaBuilder.equal(root.get("destinationCity"), city) : criteriaBuilder.conjunction();
    }
    public static Specification<Delivery> hasCourierId(String courierId) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(courierId)) {
                return criteriaBuilder.conjunction();
            }
            try {
                UUID courierUuid = UUID.fromString(courierId);
                return criteriaBuilder.equal(root.get("courier").get("id"), courierUuid);
            } catch (IllegalArgumentException e) {
                return criteriaBuilder.conjunction();
            }
        };
    }
    public static Specification<Delivery> hasSenderId(String senderId) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(senderId)) {
                return criteriaBuilder.conjunction();
            }
            try {
                UUID senderUuid = UUID.fromString(senderId);
                return criteriaBuilder.equal(root.get("sender").get("id"), senderUuid);
            } catch (IllegalArgumentException e) {
                return criteriaBuilder.conjunction();
            }
        };
    }
    public static Specification<Delivery> hasRecipientId(String recipientId) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(recipientId)) {
                return criteriaBuilder.conjunction();
            }
            try {
                UUID recipientUuid = UUID.fromString(recipientId);
                return criteriaBuilder.equal(root.get("recipient").get("id"), recipientUuid);
            } catch (IllegalArgumentException e) {
                return criteriaBuilder.conjunction();
            }
        };
    }

}

