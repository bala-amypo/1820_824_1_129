package com.example.demo.service;

import java.util.List;

import com.example.demo.model.CartItem;

public interface CartItemService {

    List<CartItem> getItems(Long cartId);

    List<CartItem> getByCartIdAndMinQuantity(Long cartId, int minQuantity);
}
