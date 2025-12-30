package com.example.demo.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.service.CartItemService;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Override
    public CartItem addItemToCart(CartItem cartItem) {

        if (cartItem == null || cartItem.getCart() == null) {
            throw new RuntimeException("CartItem or Cart cannot be null");
        }

        Cart cart = cartRepository.findById(cartItem.getCart().getId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (!cart.isActive()) {
            throw new RuntimeException("Cart is inactive");
        }

        Optional<CartItem> existingItem =
                cartItemRepository.findByCartAndProduct(
                        cart, cartItem.getProduct()
                );

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + cartItem.getQuantity());
            return cartItemRepository.save(item);
        }

        cartItem.setCart(cart);
        return cartItemRepository.save(cartItem);
    }
}
