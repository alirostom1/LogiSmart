package io.github.alirostom1.logismart.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "zones")
public class Zone extends AbstractAuditableEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "postal_code",nullable = false,unique = true)
    private int postalCode;

    @OneToMany(mappedBy = "zone",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Courier> couriers;
    @OneToMany(mappedBy = "zone",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Delivery> deliveries;
}
