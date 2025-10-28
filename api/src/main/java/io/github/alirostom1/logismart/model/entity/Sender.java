package io.github.alirostom1.logismart.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@AllArgsConstructor
@DiscriminatorValue(value = "sender")
public class Sender extends Person {

}
