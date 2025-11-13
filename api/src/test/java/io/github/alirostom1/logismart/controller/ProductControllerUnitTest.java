package io.github.alirostom1.logismart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.alirostom1.logismart.dto.request.product.CreateProductRequest;
import io.github.alirostom1.logismart.dto.response.product.ProductResponse;
import io.github.alirostom1.logismart.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
public class ProductControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String baseUrl = "/api/v2/products";

    @Test
    void index_shouldReturnAllProducts() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        ProductResponse resp = new ProductResponse(UUID.randomUUID().toString(), "item1", "cat1", 9.99);
        Page<ProductResponse> page = new PageImpl<>(Collections.singletonList(resp), pageable, 1);
        when(productService.getAllProducts(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get(baseUrl)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content", hasSize(1)));
    }

    @Test
    void show_shouldReturnProductById() throws Exception {
        String id = UUID.randomUUID().toString();
        ProductResponse resp = new ProductResponse(id, "item1", "cat1", 9.99);
        when(productService.getProductById(id)).thenReturn(resp);

        mockMvc.perform(get(baseUrl + "/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(id)));
    }

    @Test
    void store_shouldCreateProduct() throws Exception {
        CreateProductRequest request = new CreateProductRequest("item2", "cat2", 5.50);
        ProductResponse resp = new ProductResponse(UUID.randomUUID().toString(), "item2", "cat2", 5.50);
        when(productService.createProduct(any(CreateProductRequest.class))).thenReturn(resp);

        mockMvc.perform(post(baseUrl)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name", is("item2")));
    }

    @Test
    void update_shouldUpdateProduct() throws Exception {
        String id = UUID.randomUUID().toString();
        CreateProductRequest request = new CreateProductRequest("updatedItem", "cat3", 7.75);
        ProductResponse resp = new ProductResponse(id, "updatedItem", "cat3", 7.75);
        when(productService.updateProduct(anyString(), any(CreateProductRequest.class))).thenReturn(resp);

        mockMvc.perform(put(baseUrl + "/" + id)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name", is("updatedItem")));
    }

    @Test
    void delete_shouldDeleteProduct() throws Exception {
        String id = UUID.randomUUID().toString();
        mockMvc.perform(delete(baseUrl + "/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("deleted")));
    }
}
