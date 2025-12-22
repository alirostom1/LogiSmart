package io.github.alirostom1.logismart.repository;

import io.github.alirostom1.logismart.model.entity.Sender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SenderRepo extends JpaRepository<Sender,Long> {
    Optional<Sender> findByEmail(String email);
    boolean existsByEmailAndIdNot(String email,Long id);
    boolean existsByPhoneAndIdNot(String phone,Long id);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
