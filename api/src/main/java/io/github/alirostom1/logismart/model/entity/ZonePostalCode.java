package io.github.alirostom1.logismart.model.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "zone_postal_codes",
        uniqueConstraints = @UniqueConstraint(columnNames = "postal_code"))
@Getter @Setter
@NoArgsConstructor
@SuperBuilder
public class ZonePostalCode extends AbstractAuditableEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;


    @Column(name = "postal_code", nullable = false)
    private String postalCode;
}