package io.github.alirostom1.logismart.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue(value = "sender")
public class Sender extends Person {
    @OneToMany(mappedBy = "sender",fetch = FetchType.EAGER)
    private List<Delivery> deliveries;
}
