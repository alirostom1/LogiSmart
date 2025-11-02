package io.github.alirostom1.logismart.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
public class DeliveryProduct{
    @EmbeddedId
    private DeliveryProductId id;

    @Column(nullable = false)
    private int quanitity;

    @Column(nullable = false)
    private double price;

    @CreationTimestamp
    @Column(name = "added_at")
    private LocalDateTime addedAt;

    @ManyToOne
    @MapsId("deliveryId")
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;
}
