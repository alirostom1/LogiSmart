// src/main/java/io/github/alirostom1/logismart/mapper/DeliveryProductMapper.java
package io.github.alirostom1.logismart.mapper;

import io.github.alirostom1.logismart.dto.response.product.ProductInDeliveryResponse;
import io.github.alirostom1.logismart.model.entity.DeliveryProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeliveryProductMapper {

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "category", source = "product.category")
    @Mapping(target = "unitPrice", source = "product.unitPrice")
    @Mapping(target = "totalPrice", source = "price")
    ProductInDeliveryResponse toResponse(DeliveryProduct deliveryProduct);

    List<ProductInDeliveryResponse> toResponseList(List<DeliveryProduct> deliveryProducts);
}