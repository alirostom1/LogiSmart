package io.github.alirostom1.logismart.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "delivery_products",
        uniqueConstraints = @UniqueConstraint(columnNames = {"delivery_id", "product_id"}))
@Getter @Setter
@NoArgsConstructor
@SuperBuilder
public class DeliveryProduct extends AbstractAuditableEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "price_at_order",nullable = false, precision = 12, scale = 2)
    private BigDecimal priceAtOrder;
}