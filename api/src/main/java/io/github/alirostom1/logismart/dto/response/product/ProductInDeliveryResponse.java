package io.github.alirostom1.logismart.dto.response.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductInDeliveryResponse {
    private String productId;
    private String productName;
    private String category;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    private String addedAt;
}