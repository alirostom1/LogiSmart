package io.github.alirostom1.smartlogi.model.entity;


import io.github.alirostom1.smartlogi.model.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String recipient;

    @Column(nullable = false)
    private double weight;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status = DeliveryStatus.PREPARATION;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id")
    @ToString.Exclude
    private Courier courier;

    public Delivery(String recipient, double weight, String address, DeliveryStatus status) {
        this.recipient = recipient;
        this.weight = weight;
        this.address = address;
        this.status = status;
    }
}
