package io.github.alirostom1.logismart.service;

import io.github.alirostom1.logismart.dto.request.product.CreateProductRequest;
import io.github.alirostom1.logismart.dto.response.product.ProductResponse;
import io.github.alirostom1.logismart.exception.ProductNameAlreadyExistsException;
import io.github.alirostom1.logismart.exception.ResourceNotFoundException;
import io.github.alirostom1.logismart.mapper.ProductMapper;
import io.github.alirostom1.logismart.model.entity.Product;
import io.github.alirostom1.logismart.repository.ProductRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
public class ProductServiceUnitTest {
    @Mock
    ProductRepo productRepo;

    ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);
    ProductService productService;

    @BeforeEach
    public void setup(){
        productService = new ProductService(productRepo,productMapper);
    }

    //PRODUCT CREATION
    @Test
    public void createProduct_should_return_product(){
        CreateProductRequest request = new CreateProductRequest("product1","category1",15.0);
        Product mockProduct = productMapper.toEntity(request);
        ProductResponse mockProductResponse = productMapper.toResponse(mockProduct);
        when(productRepo.existsByName(request.getName())).thenReturn(false);
        when(productRepo.save(any(Product.class))).thenReturn(mockProduct);
        ProductResponse productResponse = productService.createProduct(request);
        assertEquals(mockProductResponse,productResponse);
    }
    @Test
    public void createProduct_should_throw_name_duplication_exception(){
        CreateProductRequest request = new CreateProductRequest("product1","category1",15.0);
        when(productRepo.existsByName(request.getName())).thenReturn(true);
        assertThrows(ProductNameAlreadyExistsException.class,() -> productService.createProduct(request));
    }
    //PRODUCT UPDATE
    @Test
    public void updateProduct_should_return_product(){
        UUID id = UUID.randomUUID();
        Product mockProduct = Product.builder().id(id).name("product1").category("category1").unitPrice(15.0).deliveryProduct(null).build();
        CreateProductRequest request = new CreateProductRequest("product2","category2",30.0);
        productMapper.updateFromRequest(request,mockProduct);
        ProductResponse mockProductResponse = productMapper.toResponse(mockProduct);
        when(productRepo.existsByNameAndIdNot(request.getName(),id)).thenReturn(false);
        when(productRepo.findById(id)).thenReturn(Optional.of(mockProduct));
        when(productRepo.save(any(Product.class))).thenReturn(mockProduct);
        ProductResponse productResponse = productService.updateProduct(id.toString(),request);
        assertEquals(mockProductResponse,productResponse);
    }
    @Test
    public void updateProduct_should_throw_name_duplication_exception(){
        UUID id = UUID.randomUUID();
        CreateProductRequest request = new CreateProductRequest("product2","category2",30.0);
        Product mockProduct = productMapper.toEntity(request);
        when(productRepo.findById(id)).thenReturn(Optional.of(mockProduct));
        when(productRepo.existsByNameAndIdNot(request.getName(),id)).thenReturn(true);
        assertThrows(ProductNameAlreadyExistsException.class,() -> productService.updateProduct(id.toString(),request));
    }
    @Test
    public void updateProduct_should_throw_not_found_exception(){
        UUID id = UUID.randomUUID();
        CreateProductRequest request = new CreateProductRequest("product2","category2",30.0);
        Product mockProduct = productMapper.toEntity(request);
        when(productRepo.findById(id)).thenReturn(Optional.empty());
        Exception ex = assertThrows(ResourceNotFoundException.class,() -> productService.updateProduct(id.toString(),request));
        assertTrue(ex.getMessage().contains("Product not found"));
    }

    //PRODUCT RETRIEVAL
    @Test
    public void getProductById_should_return_product(){
        UUID id = UUID.randomUUID();
        Product mockProduct = Product.builder().id(id).name("product1").category("category1").unitPrice(15.0).deliveryProduct(null).build();
        ProductResponse mockResponse = productMapper.toResponse(mockProduct);
        when(productRepo.findById(id)).thenReturn(Optional.of(mockProduct));
        ProductResponse productResponse = productService.getProductById(id.toString());
        assertEquals(mockResponse,productResponse);
    }
    @Test
    public void getProductById_should_throw_not_found_exception(){
        UUID id = UUID.randomUUID();
        when(productRepo.findById(id)).thenReturn(Optional.empty());
        Exception ex = assertThrows(ResourceNotFoundException.class,() -> productService.getProductById(id.toString()));
        assertTrue(ex.getMessage().contains("Product not found"));
    }

    //PRODUCTS RETRIEVAL
    @Test
    public void getAllProducts_should_return_products(){
        Product mockProduct1 = Product.builder().id(UUID.randomUUID()).name("product1").category("category1").unitPrice(15.0).deliveryProduct(null).build();
        Product mockProduct2 = Product.builder().id(UUID.randomUUID()).name("product2").category("category2").unitPrice(30.0).deliveryProduct(null).build();
        Pageable pageRequest = PageRequest.of(0,10);
        Page<Product> mockProductPage = new PageImpl<>(
                List.of(mockProduct1,mockProduct2)
                ,pageRequest
                ,2
        );
        Page<ProductResponse> mockResponse = mockProductPage.map(productMapper::toResponse);
        when(productRepo.findAll(pageRequest)).thenReturn(mockProductPage);
        Page<ProductResponse> responsePage = productService.getAllProducts(pageRequest);
        assertEquals(mockResponse,responsePage);
    }
    //PRODUCTS SEARCH
    @Test
    public void searchProducts_should_return_products(){
        String searchTerm = "Product 2";
        Product mockProduct1 = Product.builder().id(UUID.randomUUID()).name("product1").category("category1").unitPrice(15.0).deliveryProduct(null).build();
        Product mockProduct2 = Product.builder().id(UUID.randomUUID()).name("product2").category("category2").unitPrice(30.0).deliveryProduct(null).build();
        Pageable pageRequest = PageRequest.of(0,10);
        Page<Product> mockProductPage = new PageImpl<>(
                List.of(mockProduct1),
                pageRequest,
                1
        );
        Page<ProductResponse> mockResponsePage = mockProductPage.map(productMapper::toResponse);
        when(productRepo.findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(searchTerm,searchTerm,pageRequest)).thenReturn(mockProductPage);
        Page<ProductResponse> responsePage = productService.searchProducts(searchTerm,pageRequest);
        assertEquals(mockResponsePage,responsePage);
    }
    @Test
    public void getProductsByCategory_should_return_products() {
        String category = "Category1";
        Product mockProduct1 = Product.builder().id(UUID.randomUUID()).name("product1").category("category1").unitPrice(15.0).deliveryProduct(null).build();
        Product mockProduct2 = Product.builder().id(UUID.randomUUID()).name("product2").category("category2").unitPrice(30.0).deliveryProduct(null).build();

        Pageable pageRequest = PageRequest.of(0, 10);
        Page<Product> mockProductPage = new PageImpl<>(
                List.of(mockProduct1, mockProduct2), pageRequest, 2
        );
        Page<ProductResponse> expectedResponsePage = mockProductPage.map(productMapper::toResponse);

        when(productRepo.findByCategoryContainingIgnoreCase(category, pageRequest)).thenReturn(mockProductPage);

        Page<ProductResponse> responsePage = productService.getProductsByCategory(category, pageRequest);

        assertEquals(expectedResponsePage, responsePage);
    }
    @Test
    public void deleteProduct_should_delete_when_exists() {
        UUID productId = UUID.randomUUID();
        when(productRepo.existsById(productId)).thenReturn(true);

        productService.deleteProduct(productId.toString());

        verify(productRepo, times(1)).deleteById(productId);
    }
    @Test
    public void deleteProduct_should_throw_when_not_exists() {
        UUID productId = UUID.randomUUID();
        when(productRepo.existsById(productId)).thenReturn(false);

        Exception ex = assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(productId.toString()));
        assertTrue(ex.getMessage().contains("Product"));
        assertTrue(ex.getMessage().contains(productId.toString()));
    }
}
