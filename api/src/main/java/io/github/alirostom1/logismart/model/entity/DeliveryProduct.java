package io.github.alirostom1.logismart.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
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
}
