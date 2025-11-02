package io.github.alirostom1.logismart.dto.response.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInDeliveryResponse {
    private String productId;
    private String productName;
    private String category;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    private LocalDateTime addedAt;
}