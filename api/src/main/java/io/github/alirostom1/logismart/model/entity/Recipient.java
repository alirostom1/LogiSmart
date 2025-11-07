package io.github.alirostom1.logismart.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@DiscriminatorValue(value = "recipient")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Recipient extends Person{
    @OneToMany(mappedBy = "recipient", fetch = FetchType.EAGER)
    private List<Delivery> deliveries = new ArrayList<>();
}
