package io.github.alirostom1.logismart.model.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.alirostom1.logismart.model.enums.DeliveryPriority;
import io.github.alirostom1.logismart.model.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "deliveries")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "destination_city",nullable = false)
    private String destinationCity;

    @Column(nullable = false)
    private double weight;


    @Enumerated(EnumType.STRING)
    private DeliveryStatus status = DeliveryStatus.CREATED;

    @Enumerated(EnumType.STRING)
    private DeliveryPriority priority = DeliveryPriority.MEDIUM;


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
    private Courier courier;
}
