package io.github.alirostom1.logismart.repository;

import io.github.alirostom1.logismart.model.entity.*;
import io.github.alirostom1.logismart.model.enums.AuthProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    Optional<User> findByProviderAndProviderId(AuthProvider provider,String providerId);
}
