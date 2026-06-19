package com.app.ecom_application.dto;

import com.app.ecom_application.model.Product;
import lombok.Data;

@Data
public class CartItemRequest {
    private Long productId;
    private Integer quantity;
}
