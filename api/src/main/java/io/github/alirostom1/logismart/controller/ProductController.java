//package io.github.alirostom1.logismart.controller;
//
//import io.github.alirostom1.logismart.dto.request.product.CreateProductRequest;
//import io.github.alirostom1.logismart.dto.response.common.DefaultApiResponse;
//import io.github.alirostom1.logismart.dto.response.product.ProductResponse;
//import io.github.alirostom1.logismart.service.ProductService;
//import io.github.alirostom1.logismart.util.ValidUUID;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springdoc.core.annotations.ParameterObject;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("api/v3/products")
//@Tag(name = "Product API", description = "Operations for products")
//@RequiredArgsConstructor
//public class ProductController {
//    private final ProductService productService;
//
//
//    @Operation(summary = "Get all products with pagination")
//    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
//    @GetMapping
//    public ResponseEntity<DefaultApiResponse<Page<ProductResponse>>> index(
//            @ParameterObject Pageable pageable){
//        Page<ProductResponse> productsPage = productService.getAllProducts(pageable);
//        DefaultApiResponse<Page<ProductResponse>> defaultApiResponse = new DefaultApiResponse<>(
//                true,
//                "Products retrieved successfully",
//                productsPage,
//                System.currentTimeMillis()
//        );
//        return ResponseEntity.ok(defaultApiResponse);
//    }
//
//    @Operation(summary = "Get product by ID")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
//            @ApiResponse(responseCode = "404", description = "Product not found")
//    })
//    @GetMapping("/{id}")
//    public ResponseEntity<DefaultApiResponse<ProductResponse>> show(
//            @Parameter(description = "Product ID") @PathVariable("id") @ValidUUID String id){
//        ProductResponse productResponse = productService.getProductById(id);
//        DefaultApiResponse<ProductResponse> defaultApiResponse = new DefaultApiResponse<>(
//                true,
//                "Product Retrieved successfully",
//                productResponse,
//                System.currentTimeMillis()
//        );
//        return ResponseEntity.ok(defaultApiResponse);
//    }
//
//    @Operation(summary = "Create product")
//    @ApiResponse(responseCode = "201", description = "Product created successfully")
//    @PostMapping
//    public ResponseEntity<DefaultApiResponse<ProductResponse>> store(@Valid @RequestBody CreateProductRequest request){
//        ProductResponse productResponse = productService.createProduct(request);
//        DefaultApiResponse<ProductResponse> defaultApiResponse = new DefaultApiResponse<>(
//                true,
//                "Product created successfully!",
//                productResponse,
//                System.currentTimeMillis()
//        );
//        return ResponseEntity.status(HttpStatus.CREATED).body(defaultApiResponse);
//    }
//
//    @Operation(summary = "Update product")
//    @ApiResponses({
//            @ApiResponse(responseCode = "201", description = "Product updated successfully"),
//            @ApiResponse(responseCode = "404", description = "Product not found")
//    })
//    @PutMapping("/{id}")
//    public ResponseEntity<DefaultApiResponse<ProductResponse>> update(
//                @Parameter(description = "Product ID") @PathVariable("id") @ValidUUID String id,
//                @Valid @RequestBody CreateProductRequest request){
//        ProductResponse productResponse = productService.updateProduct(id,request);
//        DefaultApiResponse<ProductResponse> defaultApiResponse = new DefaultApiResponse<>(
//                true,
//                "Product updated successfully!",
//                productResponse,
//                System.currentTimeMillis()
//        );
//        return ResponseEntity.status(HttpStatus.CREATED).body(defaultApiResponse);
//    }
//
//    @Operation(summary = "Delete product")
//    @ApiResponse(responseCode = "200", description = "Product deleted successfully")
//    @DeleteMapping("/{id}")
//    public ResponseEntity<DefaultApiResponse<Void>> delete(
//            @Parameter(description = "Product ID") @PathVariable("id") @ValidUUID String id){
//        productService.deleteProduct(id);
//        DefaultApiResponse<Void> defaultApiResponse = new DefaultApiResponse<>(
//                true,
//                "Product deleted successfully!",
//                null,
//                System.currentTimeMillis()
//        );
//        return ResponseEntity.ok(defaultApiResponse);
//    }
//
//}
