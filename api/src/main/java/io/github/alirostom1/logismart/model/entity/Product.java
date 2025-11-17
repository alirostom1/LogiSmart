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
@Table(name = "products")
public class Product extends AbstractAuditableEntity{

    @Column(nullable = false,unique = true)
    private String name;

    private String category;

    @Column(name = "unit_price",nullable = false)
    private Double unitPrice;

    @OneToMany(mappedBy = "product",fetch = FetchType.EAGER)
    private List<DeliveryProduct> deliveryProduct;
}
