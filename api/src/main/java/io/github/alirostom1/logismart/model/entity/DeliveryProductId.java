package io.github.alirostom1.logismart.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Embeddable
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class DeliveryProductId {
    @Column(name = "product_id",nullable = false)
    private UUID productId;
    @Column(name = "delivery_id",nullable = false)
    private UUID deliveryId;

}
