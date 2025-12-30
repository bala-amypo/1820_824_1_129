package com.example.demo.service;

import java.util.List;
import com.example.demo.model.CartItem;

public interface CartItemService {

    CartItem addItem(Long cartId, Long productId, Integer quantity);

    CartItem updateItem(Long cartItemId, Integer quantity);

    List<CartItem> getItemsForCart(Long cartId);

    void removeItem(Long cartItemId);

    // used by tests
    CartItem addItemToCart(CartItem cartItem);
}
