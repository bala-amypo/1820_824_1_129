package com.example.demo.service;

import com.example.demo.model.CartItem;
import java.util.List;

public interface CartItemService {

    CartItem addItem(Long cartId, Long productId, Integer quantity);

    CartItem updateItem(Long itemId, Integer quantity);

    void removeItem(Long itemId);

    List<CartItem> getItemsForCart(Long cartId);
}
