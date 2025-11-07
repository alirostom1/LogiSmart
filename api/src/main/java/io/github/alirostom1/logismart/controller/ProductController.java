package io.github.alirostom1.logismart.controller;

import io.github.alirostom1.logismart.dto.request.product.CreateProductRequest;
import io.github.alirostom1.logismart.dto.response.common.ApiResponse;
import io.github.alirostom1.logismart.dto.response.product.ProductResponse;
import io.github.alirostom1.logismart.service.ProductService;
import io.github.alirostom1.logismart.util.ValidUUID;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v2/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> index(
            @ParameterObject Pageable pageable){
        Page<ProductResponse> productsPage = productService.getAllProducts(pageable);
        ApiResponse<Page<ProductResponse>> apiResponse = new ApiResponse<>(
                true,
                "Products retrieved successfully",
                productsPage,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> show(@PathVariable("id") @ValidUUID String id){
        ProductResponse productResponse = productService.getProductById(id);
        ApiResponse<ProductResponse> apiResponse = new ApiResponse<>(
                true,
                "Product Retrieved successfully",
                productResponse,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(apiResponse);
    }
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> store(@Valid @RequestBody CreateProductRequest request){
        ProductResponse productResponse = productService.createProduct(request);
        ApiResponse<ProductResponse> apiResponse = new ApiResponse<>(
                true,
                "Product created successfully!",
                productResponse,
                System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> update(
                @PathVariable("id") @ValidUUID String id,
                @Valid @RequestBody CreateProductRequest request){
        ProductResponse productResponse = productService.updateProduct(id,request);
        ApiResponse<ProductResponse> apiResponse = new ApiResponse<>(
                true,
                "Product updated successfully!",
                productResponse,
                System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable("id") @ValidUUID String id){
        productService.deleteProduct(id);
        ApiResponse<Void> apiResponse = new ApiResponse<>(
                true,
                "Product deleted successfully!",
                null,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(apiResponse);
    }

}
