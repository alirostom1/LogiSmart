// src/main/java/io/github/alirostom1/logismart/mapper/ProductMapper.java
package io.github.alirostom1.logismart.mapper;

import io.github.alirostom1.logismart.dto.request.product.CreateProductRequest;
import io.github.alirostom1.logismart.dto.response.product.ProductResponse;
import io.github.alirostom1.logismart.model.entity.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponse toResponse(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deliveryProduct", ignore = true)
    Product toEntity(CreateProductRequest request);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "deliveryProduct", ignore = true)
    void updateFromRequest(CreateProductRequest request,@MappingTarget Product product);
}