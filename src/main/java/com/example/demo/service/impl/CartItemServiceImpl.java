package com.example.demo.service;

import com.example.demo.model.CartItem;

import java.util.List;

public interface CartItemService {

    CartItem addItem(Long cartId, Long productId, Integer quantity);

    // REQUIRED BY TESTS
    CartItem addItemToCart(CartItem item);

    // 🔴 THIS MUST EXIST
    List<CartItem> getItemsForCart(Long cartId);

    void removeItem(Long itemId);
}
