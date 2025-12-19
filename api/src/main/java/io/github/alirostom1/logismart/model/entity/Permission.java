package io.github.alirostom1.logismart.model.entity;


import io.github.alirostom1.logismart.model.enums.EPermission;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Builder
@Table(name = "permissions")
public class Permission extends AbstractAuditableEntity{

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private EPermission name;

}