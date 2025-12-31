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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Unit Tests")
class ProductServiceUnitTest {

    @Mock
    private ProductRepo productRepo;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    // Test entities
    private Product product;
    private Sender sender;
    private CreateProductRequest createRequest;
    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
        // Setup Sender
        sender = new Sender();
        sender.setId(1L);
        sender.setFirstName("Test");
        sender.setLastName("Sender");
        sender.setEmail("sender@test.com");

        // Setup Product
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setCategory("Electronics");
        product.setUnitPrice(BigDecimal.valueOf(99.99));
        product.setSender(sender);

        // Setup CreateProductRequest
        createRequest = new CreateProductRequest();
        createRequest.setName("Test Product");
        createRequest.setCategory("Electronics");
        createRequest.setUnitPrice(BigDecimal.valueOf(99.99));

        // Setup ProductResponse
        productResponse = new ProductResponse();
        productResponse.setId(1L);
        productResponse.setName("Test Product");
        productResponse.setCategory("Electronics");
        productResponse.setUnitPrice(BigDecimal.valueOf(99.99));
    }

    // ==================== CREATE PRODUCT TESTS ====================

    @Nested
    @DisplayName("createProduct()")
    class CreateProductTests {

        @Test
        @DisplayName("Should create product successfully")
        void shouldCreateProductSuccessfully() {
            // Given
            when(productRepo.existsByNameAndSenderId("Test Product", 1L)).thenReturn(false);
            when(productMapper.toEntity(any(CreateProductRequest.class))).thenReturn(product);
            when(productRepo.save(any(Product.class))).thenReturn(product);
            when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

            // When
            ProductResponse result = productService.createProduct(createRequest, 1L);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getName()).isEqualTo("Test Product");

            verify(productRepo).existsByNameAndSenderId("Test Product", 1L);
            verify(productRepo).save(any(Product.class));
        }

        @Test
        @DisplayName("Should throw ProductNameAlreadyExistsException when name exists for sender")
        void shouldThrowExceptionWhenProductNameExistsForSender() {
            // Given
            when(productRepo.existsByNameAndSenderId("Test Product", 1L)).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> productService.createProduct(createRequest, 1L))
                    .isInstanceOf(ProductNameAlreadyExistsException.class);

            verify(productRepo, never()).save(any());
        }
    }

    // ==================== GET PRODUCT BY ID SUPER TESTS ====================

    @Nested
    @DisplayName("getProductByIdSuper()")
    class GetProductByIdSuperTests {

        @Test
        @DisplayName("Should return product when found (manager access)")
        void shouldReturnProductWhenFound() {
            // Given
            when(productRepo.findById(1L)).thenReturn(Optional.of(product));
            when(productMapper.toResponse(product)).thenReturn(productResponse);

            // When
            ProductResponse result = productService.getProductByIdSuper(1L);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            verify(productRepo).findById(1L);
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when product not found")
        void shouldThrowExceptionWhenProductNotFound() {
            // Given
            when(productRepo.findById(1L)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> productService.getProductByIdSuper(1L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Product not found");
        }
    }

    // ==================== GET PRODUCT BY ID (SENDER) TESTS ====================

    @Nested
    @DisplayName("getProductById()")
    class GetProductByIdTests {

        @Test
        @DisplayName("Should return product when sender owns it")
        void shouldReturnProductWhenSenderOwnsIt() {
            // Given
            when(productRepo.findById(1L)).thenReturn(Optional.of(product));
            when(productMapper.toResponse(product)).thenReturn(productResponse);

            // When
            ProductResponse result = productService.getProductById(1L, 1L);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Should throw UnownedRessourceException when sender doesn't own product")
        void shouldThrowExceptionWhenSenderDoesntOwnProduct() {
            // Given
            when(productRepo.findById(1L)).thenReturn(Optional.of(product));

            // When & Then
            assertThatThrownBy(() -> productService.getProductById(1L, 999L))
                    .isInstanceOf(UnownedRessourceException.class)
                    .hasMessage("You don't own this product!");
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when product not found")
        void shouldThrowExceptionWhenProductNotFound() {
            // Given
            when(productRepo.findById(1L)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> productService.getProductById(1L, 1L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Product not found");
        }
    }

    // ==================== GET MY PRODUCTS TESTS ====================

    @Nested
    @DisplayName("getMyProducts()")
    class GetMyProductsTests {

        @Test
        @DisplayName("Should return sender's products")
        void shouldReturnSendersProducts() {
            // Given
            Pageable pageable = PageRequest.of(0, 10);
            List<Product> productList = List.of(product);
            Page<Product> productPage = new PageImpl<>(productList, pageable, 1);

            when(productRepo.findBySenderId(1L, pageable)).thenReturn(productPage);
            when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

            // When
            Page<ProductResponse> result = productService.getMyProducts(1L, pageable);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            verify(productRepo).findBySenderId(1L, pageable);
        }

        @Test
        @DisplayName("Should return empty page when sender has no products")
        void shouldReturnEmptyPageWhenNoProducts() {
            // Given
            Pageable pageable = PageRequest.of(0, 10);
            Page<Product> emptyPage = new PageImpl<>(List.of(), pageable, 0);

            when(productRepo.findBySenderId(1L, pageable)).thenReturn(emptyPage);

            // When
            Page<ProductResponse> result = productService.getMyProducts(1L, pageable);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
        }
    }

    // ==================== GET ALL PRODUCTS TESTS ====================

    @Nested
    @DisplayName("getAllProducts()")
    class GetAllProductsTests {

        @Test
        @DisplayName("Should return all products paginated")
        void shouldReturnAllProductsPaginated() {
            // Given
            Pageable pageable = PageRequest.of(0, 10);
            List<Product> productList = List.of(product);
            Page<Product> productPage = new PageImpl<>(productList, pageable, 1);

            when(productRepo.findAll(pageable)).thenReturn(productPage);
            when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

            // When
            Page<ProductResponse> result = productService.getAllProducts(pageable);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getTotalElements()).isEqualTo(1);
            verify(productRepo).findAll(pageable);
        }

        @Test
        @DisplayName("Should return empty page when no products exist")
        void shouldReturnEmptyPageWhenNoProductsExist() {
            // Given
            Pageable pageable = PageRequest.of(0, 10);
            Page<Product> emptyPage = new PageImpl<>(List.of(), pageable, 0);

            when(productRepo.findAll(pageable)).thenReturn(emptyPage);

            // When
            Page<ProductResponse> result = productService.getAllProducts(pageable);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
            assertThat(result.getTotalElements()).isZero();
        }
    }

    // ==================== SEARCH PRODUCTS TESTS ====================

    @Nested
    @DisplayName("searchProducts()")
    class SearchProductsTests {

        @Test
        @DisplayName("Should return products matching search term")
        void shouldReturnProductsMatchingSearchTerm() {
            // Given
            Pageable pageable = PageRequest.of(0, 10);
            String searchTerm = "Test";
            List<Product> productList = List.of(product);
            Page<Product> productPage = new PageImpl<>(productList, pageable, 1);

            when(productRepo.findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(
                    searchTerm, searchTerm, pageable)).thenReturn(productPage);
            when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

            // When
            Page<ProductResponse> result = productService.searchProducts(searchTerm, pageable);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            verify(productRepo).findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(
                    searchTerm, searchTerm, pageable);
        }

        @Test
        @DisplayName("Should return empty page when no products match search")
        void shouldReturnEmptyPageWhenNoMatch() {
            // Given
            Pageable pageable = PageRequest.of(0, 10);
            String searchTerm = "NonExistent";
            Page<Product> emptyPage = new PageImpl<>(List.of(), pageable, 0);

            when(productRepo.findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(
                    searchTerm, searchTerm, pageable)).thenReturn(emptyPage);

            // When
            Page<ProductResponse> result = productService.searchProducts(searchTerm, pageable);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
        }
    }

    // ==================== GET PRODUCTS BY CATEGORY TESTS ====================

    @Nested
    @DisplayName("getProductsByCategory()")
    class GetProductsByCategoryTests {

        @Test
        @DisplayName("Should return products in category")
        void shouldReturnProductsInCategory() {
            // Given
            Pageable pageable = PageRequest.of(0, 10);
            String category = "Electronics";
            List<Product> productList = List.of(product);
            Page<Product> productPage = new PageImpl<>(productList, pageable, 1);

            when(productRepo.findByCategoryContainingIgnoreCase(category, pageable)).thenReturn(productPage);
            when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

            // When
            Page<ProductResponse> result = productService.getProductsByCategory(category, pageable);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            verify(productRepo).findByCategoryContainingIgnoreCase(category, pageable);
        }

        @Test
        @DisplayName("Should return empty page when no products in category")
        void shouldReturnEmptyPageWhenNoCategoryMatch() {
            // Given
            Pageable pageable = PageRequest.of(0, 10);
            String category = "NonExistentCategory";
            Page<Product> emptyPage = new PageImpl<>(List.of(), pageable, 0);

            when(productRepo.findByCategoryContainingIgnoreCase(category, pageable)).thenReturn(emptyPage);

            // When
            Page<ProductResponse> result = productService.getProductsByCategory(category, pageable);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
        }
    }

    // ==================== UPDATE PRODUCT TESTS ====================

    @Nested
    @DisplayName("updateProduct()")
    class UpdateProductTests {

        @Test
        @DisplayName("Should update product successfully")
        void shouldUpdateProductSuccessfully() {
            // Given
            CreateProductRequest updateRequest = new CreateProductRequest();
            updateRequest.setName("Updated Product");
            updateRequest.setCategory("Updated Category");
            updateRequest.setUnitPrice(BigDecimal.valueOf(149.99));

            ProductResponse updatedResponse = new ProductResponse();
            updatedResponse.setId(1L);
            updatedResponse.setName("Updated Product");

            when(productRepo.findById(1L)).thenReturn(Optional.of(product));
            when(productRepo.existsByNameAndIdNotAndSenderId("Updated Product", 1L, 1L)).thenReturn(false);
            when(productRepo.save(any(Product.class))).thenReturn(product);
            when(productMapper.toResponse(any(Product.class))).thenReturn(updatedResponse);

            // When
            ProductResponse result = productService.updateProduct(1L, updateRequest, 1L);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Updated Product");

            verify(productMapper).updateFromRequest(updateRequest, product);
            verify(productRepo).save(product);
        }

        @Test
        @DisplayName("Should throw UnownedRessourceException when sender doesn't own product")
        void shouldThrowExceptionWhenSenderDoesntOwnProduct() {
            // Given
            when(productRepo.findById(1L)).thenReturn(Optional.of(product));

            // When & Then
            assertThatThrownBy(() -> productService.updateProduct(1L, createRequest, 999L))
                    .isInstanceOf(UnownedRessourceException.class)
                    .hasMessage("You don't have permission!");

            verify(productRepo, never()).save(any());
        }

        @Test
        @DisplayName("Should throw ProductNameAlreadyExistsException when name exists for another product")
        void shouldThrowExceptionWhenNameExistsForAnotherProduct() {
            // Given
            when(productRepo.findById(1L)).thenReturn(Optional.of(product));
            when(productRepo.existsByNameAndIdNotAndSenderId("Test Product", 1L, 1L)).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> productService.updateProduct(1L, createRequest, 1L))
                    .isInstanceOf(ProductNameAlreadyExistsException.class);

            verify(productRepo, never()).save(any());
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when product not found")
        void shouldThrowExceptionWhenProductNotFound() {
            // Given
            when(productRepo.findById(1L)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> productService.updateProduct(1L, createRequest, 1L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Product not found");

            verify(productRepo, never()).save(any());
        }
    }

    // ==================== DELETE PRODUCT SUPER TESTS ====================

    @Nested
    @DisplayName("deleteProductSuper()")
    class DeleteProductSuperTests {

        @Test
        @DisplayName("Should delete product successfully (manager access)")
        void shouldDeleteProductSuccessfully() {
            // Given
            when(productRepo.existsById(1L)).thenReturn(true);
            doNothing().when(productRepo).deleteById(1L);

            // When
            productService.deleteProductSuper(1L);

            // Then
            verify(productRepo).existsById(1L);
            verify(productRepo).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when product not found")
        void shouldThrowExceptionWhenProductNotFound() {
            // Given
            when(productRepo.existsById(1L)).thenReturn(false);

            // When & Then
            assertThatThrownBy(() -> productService.deleteProductSuper(1L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Product not found");

            verify(productRepo, never()).deleteById(anyLong());
        }
    }

    // ==================== DELETE PRODUCT (SENDER) TESTS ====================

    @Nested
    @DisplayName("deleteProduct()")
    class DeleteProductTests {

        @Test
        @DisplayName("Should delete product when sender owns it")
        void shouldDeleteProductWhenSenderOwnsIt() {
            // Given
            when(productRepo.existsByIdAndSenderId(1L, 1L)).thenReturn(true);
            doNothing().when(productRepo).deleteById(1L);

            // When
            productService.deleteProduct(1L, 1L);

            // Then
            verify(productRepo).existsByIdAndSenderId(1L, 1L);
            verify(productRepo).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when product not found or not owned")
        void shouldThrowExceptionWhenProductNotFoundOrNotOwned() {
            // Given
            when(productRepo.existsByIdAndSenderId(1L, 1L)).thenReturn(false);

            // When & Then
            assertThatThrownBy(() -> productService.deleteProduct(1L, 1L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Product not found");

            verify(productRepo, never()).deleteById(anyLong());
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when sender doesn't own product")
        void shouldThrowExceptionWhenSenderDoesntOwnProduct() {
            // Given
            when(productRepo.existsByIdAndSenderId(1L, 999L)).thenReturn(false);

            // When & Then
            assertThatThrownBy(() -> productService.deleteProduct(1L, 999L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Product not found");

            verify(productRepo, never()).deleteById(anyLong());
        }
    }
}