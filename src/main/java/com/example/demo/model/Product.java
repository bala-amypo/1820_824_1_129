package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Product {

    private Long id;
    private String sku;
    private String name;
    private String category;
    private BigDecimal price;
    private Boolean active;
}
