package com.example.demo.service.impl;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.CartItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {
private final CartItemRepository cartItemRepository;
private final CartRepository cartRepository;
private final ProductRepository productRepository;

public CartItemServiceImpl(
        CartItemRepository cartItemRepository,
        CartRepository cartRepository,
        ProductRepository productRepository
) {
    this.cartItemRepository = cartItemRepository;
    this.cartRepository = cartRepository;
    this.productRepository = productRepository;
}

@Override
public CartItem addItem(Long cartId, Long productId, int quantity) {

    if (quantity <= 0) {
        throw new IllegalArgumentException("Quantity must be positive");
    }

    Cart cart = cartRepository.findById(cartId).orElse(null);
    if (cart == null || !cart.getActive()) {
        throw new IllegalArgumentException("Cart inactive");
    }

    Product product = productRepository.findById(productId).orElse(null);

    CartItem existing = cartItemRepository
            .findByCartIdAndProductId(cartId, productId)
            .orElse(null);

    if (existing != null) {
        existing.setQuantity(existing.getQuantity() + quantity);
        return cartItemRepository.save(existing);
    }

    CartItem item = new CartItem();
    item.setCart(cart);
    item.setProduct(product);
    item.setQuantity(quantity);

    return cartItemRepository.save(item);
}

@Override
public void removeItem(Long id) {
    cartItemRepository.deleteById(id);
}

@Override
public List<CartItem> getItemsForCart(Long cartId) {
    return cartItemRepository.findByCartId(cartId);
}
}