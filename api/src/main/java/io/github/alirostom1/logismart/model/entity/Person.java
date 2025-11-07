package io.github.alirostom1.logismart.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type",discriminatorType = DiscriminatorType.STRING)
@Table(name = "persons")
public abstract class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "last_name",nullable = false)
    private String lastName;

    @Column(name = "first_name",nullable = false)
    private String firstName;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false,unique = false)
    private String phone;

    @Column(nullable = false)
    private String address;

}
