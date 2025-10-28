package io.github.alirostom1.logismart.repository;

import io.github.alirostom1.logismart.model.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PersonRepo extends JpaRepository<Person, UUID> {

}
