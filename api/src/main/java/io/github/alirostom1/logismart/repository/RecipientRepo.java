package io.github.alirostom1.logismart.repository;

import io.github.alirostom1.logismart.model.entity.Recipient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecipientRepo extends JpaRepository<Recipient,Long> {
    Optional<Recipient> findByPhone(String phone);
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByPhoneAndIdNot(String phone, Long id);
}
