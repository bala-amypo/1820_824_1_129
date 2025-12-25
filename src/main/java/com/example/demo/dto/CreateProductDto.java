package com.example.demo.dto;

import java.math.BigDecimal;

public class CreateProductDto {
    private String sku;
    private String name;
    private BigDecimal price;
    
    // Constructors
    public CreateProductDto() {}
    
    public CreateProductDto(String sku, String name, BigDecimal price) {
        this.sku = sku;
        this.name = name;
        this.price = price;
    }
    
    // Getters and Setters
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
