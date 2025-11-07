package io.github.alirostom1.logismart.service;

import io.github.alirostom1.logismart.dto.request.product.CreateProductRequest;
import io.github.alirostom1.logismart.dto.response.product.ProductResponse;
import io.github.alirostom1.logismart.exception.ProductNameAlreadyExistsException;
import io.github.alirostom1.logismart.exception.ResourceNotFoundException;
import io.github.alirostom1.logismart.mapper.ProductMapper;
import io.github.alirostom1.logismart.model.entity.Product;
import io.github.alirostom1.logismart.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final ProductMapper productMapper;

    // CREATE PRODUCT (FOR MANAGER)
    public ProductResponse createProduct(CreateProductRequest request) {
        if (productRepo.existsByName(request.getName())) {
            throw new ProductNameAlreadyExistsException(request.getName());
        }

        Product product = productMapper.toEntity(request);
        Product savedProduct = productRepo.save(product);
        return productMapper.toResponse(savedProduct);
    }

    // GET PRODUCT BY ID (FOR MANAGER)
    @Transactional(readOnly = true)
    public ProductResponse getProductById(String productId) {
        Product product = findById(productId);
        return productMapper.toResponse(product);
    }

    // GET ALL PRODUCTS (FOR MANAGER)
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepo.findAll(pageable);
        return products.map(productMapper::toResponse);
    }

    // SEARCH PRODUCTS BY NAME OR CATEGORY (FOR MANAGER)
    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProducts(String searchTerm, Pageable pageable) {
        Page<Product> products = productRepo.findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(
                searchTerm, searchTerm, pageable);
        return products.map(productMapper::toResponse);
    }

    // GET PRODUCTS BY CATEGORY (FOR MANAGER)
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsByCategory(String category, Pageable pageable) {
        Page<Product> products = productRepo.findByCategoryContainingIgnoreCase(category, pageable);
        return products.map(productMapper::toResponse);
    }

    // UPDATE PRODUCT (FOR MANAGER)
    public ProductResponse updateProduct(String productId, CreateProductRequest request) {
        Product product = findById(productId);

        if (productRepo.existsByNameAndIdNot(request.getName(), UUID.fromString(productId))) {
            throw new ProductNameAlreadyExistsException(request.getName());
        }

        productMapper.updateFromRequest(request,product);

        Product updatedProduct = productRepo.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    // DELETE PRODUCT (FOR MANAGER)
    public void deleteProduct(String productId) {
        if (!productRepo.existsById(UUID.fromString(productId))) {
            throw new ResourceNotFoundException("Product", productId);
        }
        productRepo.deleteById(UUID.fromString(productId));
    }

    // UTIL METHOD
    private Product findById(String productId) {
        return productRepo.findById(UUID.fromString(productId))
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId));
    }
}