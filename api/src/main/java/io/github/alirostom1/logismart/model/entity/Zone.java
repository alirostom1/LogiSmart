package io.github.alirostom1.logismart.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "zones")
public class Zone extends AbstractAuditableEntity{
    @Column(nullable = false)
    private String name;

    @Column(length = 10)
    private String code; // PREFIXES FOR TRACKING PURPOSES

    private boolean active = true;

    @OneToMany(mappedBy = "zone",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Courier> couriers;
    @OneToMany(mappedBy = "pickupZone", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Delivery> pickups = new ArrayList<>();


    @OneToMany(mappedBy = "shippingZone", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Delivery> shipments = new ArrayList<>();

    @OneToMany(mappedBy = "zone", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ZonePostalCode> postalCodes = new HashSet<>();
}
