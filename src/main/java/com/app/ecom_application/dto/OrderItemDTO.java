package com.app.ecom_application.dto;

import com.app.ecom_application.model.Order;
import com.app.ecom_application.model.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class OrderItemDTO {

    private Long id;
    private Long productId;
    private Integer quantity;
    private BigDecimal price;
}
