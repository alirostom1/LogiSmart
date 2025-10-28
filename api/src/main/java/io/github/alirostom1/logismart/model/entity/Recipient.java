package io.github.alirostom1.logismart.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Entity
@DiscriminatorValue(value = "recipient")
public class Recipient extends Person{
    @OneToMany(mappedBy = "recipient", fetch = FetchType.EAGER)
    private List<Delivery> deliveries;
}
