package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItem {

    private Long id;
    private Cart cart;
    private Product product;
    private Integer quantity;
}
