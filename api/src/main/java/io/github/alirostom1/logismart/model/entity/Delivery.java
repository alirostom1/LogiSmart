package io.github.alirostom1.logismart.model.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.alirostom1.logismart.model.enums.DeliveryPriority;
import io.github.alirostom1.logismart.model.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
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

    @ManyToOne
    @JoinColumn(name = "zone_id",nullable = false)
    private Zone zone;

    @ManyToOne
    @JoinColumn(name = "recipient_id",nullable = false)
    private Recipient recipient;

    @ManyToOne
    @JoinColumn(name = "sender_id",nullable = false)
    private Sender sender;

    @OneToMany(mappedBy = "delivery",fetch = FetchType.EAGER)
    private List<DeliveryProduct> deliveryProducts;


    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "collecting_courier_id")
    private Courier collectingCourier;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shipping_courier_id")
    private Courier shippingCourier;

    @OneToMany(mappedBy = "delivery",fetch = FetchType.EAGER)
    private List<DeliveryHistory> deliveryHistoryList;


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
