package io.github.alirostom1.logismart.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "recipients",uniqueConstraints = @UniqueConstraint(columnNames = {"phone", "postal_code"}))
public class Recipient extends AbstractAuditableEntity{

    @Column(nullable = false)
    private String name;

    @Column(nullable = false,unique = true)
    private String phone;

    @Column(nullable = false,unique = true)
    private String email;

    @OneToMany(mappedBy = "recipient", fetch = FetchType.EAGER)
    @Builder.Default
    private List<Delivery> deliveries = new ArrayList<>();
}
