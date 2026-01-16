package io.github.alirostom1.logismart.model.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.alirostom1.logismart.model.enums.DeliveryPriority;
import io.github.alirostom1.logismart.model.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "deliveries")
public class Delivery extends AbstractAuditableEntity{
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "tracking_number", nullable = false, unique = true)
    private String trackingNumber; // FOR PUBLIC ENDPOINTS INSTEAD OF DISPLAYING ID IN URL

    @Column(name = "weight_kg", nullable = false,precision = 10,scale = 3)
    private BigDecimal weightKg;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status = DeliveryStatus.CREATED;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryPriority priority = DeliveryPriority.MEDIUM;


    @ManyToOne
    @JoinColumn(name = "recipient_id",nullable = false)
    private Recipient recipient;

    @ManyToOne
    @JoinColumn(name = "sender_id",nullable = false)
    private Sender sender;

    @OneToMany(mappedBy = "delivery", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryProduct> deliveryProducts = new ArrayList<>();



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collecting_courier_id")
    private Courier collectingCourier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_courier_id")
    private Courier shippingCourier;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pickup_zone_id", nullable = false)
    private Zone pickupZone;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shipping_zone_id", nullable = false)
    private Zone shippingZone;

    @Column(name = "pickup_address", nullable = false)
    private String pickupAddress;

    @Column(name = "pickup_postal_code", nullable = false)
    private String pickupPostalCode;

    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;

    @Column(name = "shipping_postal_code", nullable = false)
    private String shippingPostalCode;

    @OneToMany(mappedBy = "delivery", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DeliveryHistory> deliveryHistoryList = new ArrayList<>();
}
