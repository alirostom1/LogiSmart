package io.github.alirostom1.logismart.repository;

import io.github.alirostom1.logismart.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepo extends JpaRepository<Product, UUID> {
}
