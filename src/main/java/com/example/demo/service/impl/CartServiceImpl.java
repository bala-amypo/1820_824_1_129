package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Cart;
import com.example.demo.repository.CartRepository;
import com.example.demo.service.CartService;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }
    public Cart getActiveCartForUser(Long userId) {
    return cartRepository.findByUserIdAndActiveTrue(userId)
            .orElseThrow(() ->
                    new jakarta.persistence.EntityNotFoundException(
                            "Active cart not found"));
}


    @Override
    public Cart createCart(Long userId) {
        cartRepository.findByUserId(userId)
                .ifPresent(c -> {
                    throw new IllegalArgumentException("Cart already exists");
                });

        Cart cart = new Cart();
        cart.setUserId(userId);
        return cartRepository.save(cart);
    }

    @Override
    public Cart getCartById(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart not found"));
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart not found"));
    }

    @Override
    public void deactivateCart(Long id) {
        Cart cart = getCartById(id);
        cart.setActive(false);
        cartRepository.save(cart);
    }
}
