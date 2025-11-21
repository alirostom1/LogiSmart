package io.github.alirostom1.logismart.service;

import io.github.alirostom1.logismart.dto.request.product.CreateProductRequest;
import io.github.alirostom1.logismart.dto.response.product.ProductResponse;
import io.github.alirostom1.logismart.exception.ProductNameAlreadyExistsException;
import io.github.alirostom1.logismart.exception.ResourceNotFoundException;
import io.github.alirostom1.logismart.exception.UnownedRessourceException;
import io.github.alirostom1.logismart.mapper.ProductMapper;
import io.github.alirostom1.logismart.model.entity.Product;
import io.github.alirostom1.logismart.model.entity.Sender;
import io.github.alirostom1.logismart.repository.ProductRepo;
import io.github.alirostom1.logismart.util.SlugGenerator;
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

    // CREATE PRODUCT (FOR SENDER)
    public ProductResponse createProduct(CreateProductRequest request,Long senderId) {
        if (productRepo.existsByNameAndSenderId(request.getName(),senderId)) {
            throw new ProductNameAlreadyExistsException(request.getName());
        }
        Product product = productMapper.toEntity(request);
        product.setSender(Sender.builder().id(senderId).build());
        Product savedProduct = productRepo.save(product);
        return productMapper.toResponse(savedProduct);
    }

    // GET PRODUCT BY ID (FOR MANAGER)
    @Transactional(readOnly = true)
    public ProductResponse getProductByIdSuper(Long productId) {
        Product product = findById(productId);
        return productMapper.toResponse(product);
    }

    //GET OWNED PRODUCT BY ID (FOR SENDER)
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId,Long userId){
        Product product = findById(productId);
        if(product.getSender().getId().equals(userId)){
            throw new UnownedRessourceException("You don't own this product!");
        }
        return productMapper.toResponse(product);
    }

    //GET SENDER'S PRODUCTS(FOR SENDER)
    @Transactional(readOnly = true)
    public Page<ProductResponse> getMyProducts(Long userId,Pageable pageable){
        Page<Product> products = productRepo.findBySenderId(userId,pageable);
        return products.map(productMapper::toResponse);
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

    // UPDATE PRODUCT (FOR Sender)
    public ProductResponse updateProduct(Long productId, CreateProductRequest request,Long senderId) {
        Product product = findById(productId);
        if(!product.getSender().getId().equals(senderId)){
            throw new UnownedRessourceException("You don't have permission!");
        }
        if (productRepo.existsByNameAndIdNotAndSenderId(request.getName(), productId,senderId)) {
            throw new ProductNameAlreadyExistsException(request.getName());
        }

        productMapper.updateFromRequest(request,product);

        Product updatedProduct = productRepo.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    // DELETE PRODUCT (FOR MANAGER)
    public void deleteProductSuper(Long productId) {
        if (!productRepo.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found");
        }
        productRepo.deleteById(productId);
    }

    // DELETE PRODUCT (FOR SENDER)
    public void deleteProduct(Long productId,Long senderId) {
        if (!productRepo.existsByIdAndSenderId(productId,senderId)) {
            throw new ResourceNotFoundException("Product not found");
        }
        productRepo.deleteById(productId);
    }



    // UTIL METHOD
    private Product findById(Long productId) {
        return productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }
}