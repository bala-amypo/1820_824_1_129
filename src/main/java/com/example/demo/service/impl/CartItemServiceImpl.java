package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

import com.example.demo.model.Cart;
import com.example.demo.repository.CartRepository;
import com.example.demo.service.CartService;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart createCart(Long userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setActive(true);
        return cartRepository.save(cart);
    }

    @Override
    public Cart getActiveCartForUser(Long userId) {
        return cartRepository.findByUserIdAndActive(userId, true);
    }
}
