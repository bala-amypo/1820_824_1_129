package com.example.demo.service;

import com.example.demo.model.Cart;

public interface CartService {

    Cart createCart(Long userId);

    Cart getCartById(Long id);

    Cart getCartByUserId(Long userId);

    void deactivateCart(Long id);
}
