package com.example.demo.service.impl;
import org.springframework.stereotype.Service;
import com.example.demo.model.Cart;
import com.example.demo.repository.CartRepository;
import com.example.demo.service.CartService;

import jakarta.persistence.EntityNotFoundException;
@Service
public class CartServiceImpl implements CartService {

    private final CartRepository repository;

    public CartServiceImpl(CartRepository repository) {
        this.repository = repository;
    }

    @Override
    public Cart createCart(Long userId) {
        return repository.findByUserIdAndActiveTrue(userId)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUserId(userId);
                    cart.setActive(true);
                    return repository.save(cart);
                });
    }

    @Override
    public Cart getCartById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return repository.findByUserIdAndActiveTrue(userId)
                .orElseThrow(() -> new EntityNotFoundException("Active cart not found"));
    }

    @Override
    public void deactivateCart(Long id) {
        Cart cart = getCartById(id);
        cart.setActive(false);
        repository.save(cart);
    }
}
