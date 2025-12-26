package com.example.demo.service.impl;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.service.CartItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
private final CartRepository cartRepository;

public CartItemServiceImpl(
        CartItemRepository cartItemRepository,
        CartRepository cartRepository
) {
    this.cartItemRepository = cartItemRepository;
    this.cartRepository = cartRepository;
}

@Override
public CartItem addItemToCart(CartItem item) {
    Cart cart = cartRepository.findById(item.getCart().getId()).orElseThrow();
    if (!cart.getActive()) {
        throw new IllegalArgumentException("Cart is inactive");
    }
    return cartItemRepository.save(item);
}

@Override
public void removeItem(Long itemId) {
    cartItemRepository.deleteById(itemId);
}

@Override
public CartItem updateItem(Long itemId, Integer quantity) {
    if (quantity <= 0) {
        throw new IllegalArgumentException("Quantity must be positive");
    }
    CartItem item = cartItemRepository.findById(itemId).orElseThrow();
    item.setQuantity(quantity);
    return cartItemRepository.save(item);
}

@Override
public List<CartItem> getItemsForCart(Long cartId) {
    return cartItemRepository.findByCartId(cartId);
}
