package io.github.alirostom1.logismart.controller;

import io.github.alirostom1.logismart.dto.request.product.CreateProductRequest;
import io.github.alirostom1.logismart.dto.response.common.DefaultApiResponse;
import io.github.alirostom1.logismart.dto.response.product.ProductResponse;
import io.github.alirostom1.logismart.model.entity.User;
import io.github.alirostom1.logismart.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v3/products")
@Tag(name = "Product API", description = "Operations for products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "Get all products with pagination")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    @GetMapping
    @PreAuthorize("hasAuthority('PRODUCT_READ')")
    public ResponseEntity<DefaultApiResponse<Page<ProductResponse>>> index(
            @ParameterObject Pageable pageable){
        Page<ProductResponse> productsPage = productService.getAllProducts(pageable);
        DefaultApiResponse<Page<ProductResponse>> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Products retrieved successfully",
                productsPage,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }

    @Operation(summary = "Get product by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PRODUCT_READ') or hasAuthority('PRODUCT_READ_OWN')")
    public ResponseEntity<DefaultApiResponse<ProductResponse>> show(
            @Parameter(description = "Product ID") @PathVariable("id") Long id){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ProductResponse productResponse;
        if (user.getRole().getName().name().equals("ROLE_SENDER")) {
            productResponse = productService.getProductById(id, user.getId());
        } else {
            productResponse = productService.getProductByIdSuper(id);
        }
        DefaultApiResponse<ProductResponse> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Product Retrieved successfully",
                productResponse,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }

    @Operation(summary = "Get my products (for sender)")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    @GetMapping("/my-products")
    @PreAuthorize("hasAuthority('PRODUCT_READ_OWN')")
    public ResponseEntity<DefaultApiResponse<Page<ProductResponse>>> getMyProducts(
            @ParameterObject Pageable pageable){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<ProductResponse> productsPage = productService.getMyProducts(user.getId(), pageable);
        DefaultApiResponse<Page<ProductResponse>> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Products retrieved successfully",
                productsPage,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }

    @Operation(summary = "Create product")
    @ApiResponse(responseCode = "201", description = "Product created successfully")
    @PostMapping
    @PreAuthorize("hasAuthority('PRODUCT_SAVE')")
    public ResponseEntity<DefaultApiResponse<ProductResponse>> store(@Valid @RequestBody CreateProductRequest request){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ProductResponse productResponse = productService.createProduct(request, user.getId());
        DefaultApiResponse<ProductResponse> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Product created successfully!",
                productResponse,
                System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(defaultApiResponse);
    }

    @Operation(summary = "Update product")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PRODUCT_SAVE')")
    public ResponseEntity<DefaultApiResponse<ProductResponse>> update(
                @Parameter(description = "Product ID") @PathVariable("id") Long id,
                @Valid @RequestBody CreateProductRequest request){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ProductResponse productResponse = productService.updateProduct(id, request, user.getId());
        DefaultApiResponse<ProductResponse> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Product updated successfully!",
                productResponse,
                System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(defaultApiResponse);
    }

    @Operation(summary = "Delete product")
    @ApiResponse(responseCode = "200", description = "Product deleted successfully")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PRODUCT_DELETE') or hasAuthority('PRODUCT_READ_OWN')")
    public ResponseEntity<DefaultApiResponse<Void>> delete(
            @Parameter(description = "Product ID") @PathVariable("id") Long id){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getRole().getName().name().equals("ROLE_SENDER")) {
            productService.deleteProduct(id, user.getId());
        } else {
            productService.deleteProductSuper(id);
        }
        DefaultApiResponse<Void> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Product deleted successfully!",
                null,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }

}
