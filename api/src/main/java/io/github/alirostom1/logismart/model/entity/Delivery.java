package io.github.alirostom1.logismart.model.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.alirostom1.logismart.model.enums.DeliveryStatus;
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
@Table(name = "deliveries")
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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "courier_id")
    @ToString.Exclude
    private Courier courier;

    public Delivery(String recipient, double weight, String address,DeliveryStatus status) {
        this.recipient = recipient;
        this.weight = weight;
        this.address = address;
        this.status = status;
    }
}
