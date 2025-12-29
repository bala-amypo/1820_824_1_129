package com.example.demo.service.impl;

import com.example.demo.model.Cart;
import com.example.demo.repository.CartRepository;
import jakarta.persistence.EntityNotFoundException;

public class CartServiceImpl {

    private final CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart createCart(Long userId) {
        if (cartRepository.findByUserIdAndActiveTrue(userId).isPresent()) {
            throw new IllegalArgumentException("Cart already exists");
        }
        Cart c = new Cart();
        c.setUserId(userId);
        return cartRepository.save(c);
    }

    public Cart getActiveCartForUser(Long userId) {
        return cartRepository.findByUserIdAndActiveTrue(userId)
                .orElseThrow(() -> new EntityNotFoundException("Active cart not found"));
    }
}
