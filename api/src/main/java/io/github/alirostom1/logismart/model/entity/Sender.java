package io.github.alirostom1.logismart.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@DiscriminatorValue(value = "sender")
public class Sender extends Person {
    @OneToMany(mappedBy = "sender",fetch = FetchType.EAGER)
    private List<Delivery> deliveries = new ArrayList<>();
}
