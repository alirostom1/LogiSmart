package io.github.alirostom1.logismart.dto.response.deliveryproduct;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonKey;
import io.github.alirostom1.logismart.dto.response.product.ProductSimpleResponse;

public record DeliveryProductResponse(
        String id,
        double price,
        String addedAt,
        ProductSimpleResponse product
){
}
