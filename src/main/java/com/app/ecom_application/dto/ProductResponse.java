package com.app.ecom_application.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private Integer stockQuantity;
    private BigDecimal price;
    private String category;
    private String imageUrl;
    private boolean active;
}
