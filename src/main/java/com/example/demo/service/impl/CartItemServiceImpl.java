package com.example.demo.service.impl;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.service.CartItemService;
import org.springframework.stereotype.Service;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepo;
    private final CartRepository cartRepo;

    public CartItemServiceImpl(CartItemRepository cartItemRepo,
                               CartRepository cartRepo) {
        this.cartItemRepo = cartItemRepo;
        this.cartRepo = cartRepo;
    }

    @Override
    public CartItem addItemToCart(CartItem item) {

        if (item.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        Cart cart = cartRepo.findById(item.getCart().getId())
                .orElseThrow(IllegalArgumentException::new);

        if (!cart.getActive()) {
            throw new IllegalArgumentException("Cart is inactive");
        }

        CartItem existing = cartItemRepo
                .findByCartAndProduct(cart, item.getProduct())
                .orElse(null);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + item.getQuantity());
            return cartItemRepo.save(existing);
        }

        return cartItemRepo.save(item);
    }
}
