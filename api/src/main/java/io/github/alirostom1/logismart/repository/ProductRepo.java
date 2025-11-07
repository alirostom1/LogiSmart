package io.github.alirostom1.logismart.repository;

import io.github.alirostom1.logismart.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepo extends JpaRepository<Product, UUID> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name,UUID id);

    Page<Product> findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(String name, String category, Pageable pageable);
    Page<Product> findByCategoryContainingIgnoreCase(String category,Pageable pageable);
}
