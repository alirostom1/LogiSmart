package io.github.alirostom1.logismart.repository;

import io.github.alirostom1.logismart.model.entity.Person;
import io.github.alirostom1.logismart.model.entity.Recipient;
import io.github.alirostom1.logismart.model.entity.Sender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PersonRepo extends JpaRepository<Person, UUID> {
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsByPhoneAndIdNot(String phone,UUID id);
    boolean existsByEmailAndIdNot(String email,UUID id);


    @Query("SELECT p FROM Person p WHERE p.id = :id AND TYPE(p) = Sender")
    Optional<Sender> findSenderById(@Param("id") UUID id);

    @Query("SELECT p FROM Person p WHERE p.id = :id AND TYPE(p) = Recipient")
    Optional<Recipient> findRecipientById(@Param("id") UUID id);

    @Query("SELECT p FROM Person p WHERE TYPE(p) = Sender")
    Page<Sender> findSenders(Pageable pageable);

    @Query("SELECT p FROM Person p WHERE TYPE(p) = Recipient")
    Page<Recipient> findRecipients(Pageable pageable);

}
