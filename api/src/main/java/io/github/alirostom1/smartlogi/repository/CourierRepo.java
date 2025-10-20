package io.github.alirostom1.smartlogi.repository;

import io.github.alirostom1.smartlogi.model.entity.Courier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CourierRepo extends JpaRepository<Courier, UUID> {
}
