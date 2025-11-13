package io.github.alirostom1.logismart.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@DiscriminatorValue(value = "sender")
public class Sender extends Person {
    @OneToMany(mappedBy = "sender",fetch = FetchType.EAGER)
    private List<Delivery> deliveries = new ArrayList<>();

    public Sender(UUID id, String lastName, String firstName, String email, String phone, String address, List<Delivery> deliveries) {
        super(id, lastName, firstName, email, phone, address);
        this.deliveries = deliveries;
    }
}
