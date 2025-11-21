package io.github.alirostom1.logismart.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type",discriminatorType = DiscriminatorType.STRING)
@Table(name = "users")
public class User extends AbstractAuditableEntity implements UserDetails{
    @Column(name = "last_name",nullable = false)
    private String lastName;

    @Column(name = "first_name",nullable = false)
    private String firstName;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(name = "password_hash",nullable = false, columnDefinition = "TEXT")
    private String password;

    @Column(nullable = false,unique = false)
    private String phone;

    @Column(name = "type", insertable = false, updatable = false)
    private String role;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null) {
            DiscriminatorValue discriminatorValue = this.getClass().getAnnotation(DiscriminatorValue.class);
            String fallbackRole = discriminatorValue != null ? discriminatorValue.value() : "user";
            return List.of(new SimpleGrantedAuthority("ROLE_" + fallbackRole.toUpperCase()));
        }
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
