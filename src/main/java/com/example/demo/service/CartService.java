package com.example.demo.service;

import com.example.demo.model.Cart;

public interface CartService {

    Cart createCart(Long userId);

    Cart getCartByUserId(Long userId);

    Cart getCartById(Long id);

    void deactivateCart(Long id);
}
