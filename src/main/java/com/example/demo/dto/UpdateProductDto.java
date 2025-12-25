package com.example.demo.dto;

import java.math.BigDecimal;

public class UpdateProductDto {
    private String sku;
    private String name;
    private BigDecimal price;
    
    // Constructors, Getters and Setters (same as CreateProductDto)
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
