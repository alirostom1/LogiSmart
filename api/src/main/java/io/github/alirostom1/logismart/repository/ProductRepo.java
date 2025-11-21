package io.github.alirostom1.logismart.repository;

import io.github.alirostom1.logismart.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepo extends JpaRepository<Product, Long> {
    boolean existsByName(String name);
    boolean existsByNameAndSenderId(String name,Long senderId);
    boolean existsByNameAndIdNotAndSenderId(String name,Long id,Long senderId);
    boolean existsByNameAndIdNot(String name,Long id);
    boolean existsByIdAndSenderId(Long id,Long senderId);

    Page<Product> findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(String name, String category, Pageable pageable);
    Page<Product> findByCategoryContainingIgnoreCase(String category,Pageable pageable);

    Page<Product> findBySenderId(Long senderId,Pageable pageable);
}
