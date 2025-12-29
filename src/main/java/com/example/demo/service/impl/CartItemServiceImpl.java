package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public class CartItemServiceImpl {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartItemServiceImpl(
            CartItemRepository cartItemRepository,
            CartRepository cartRepository,
            ProductRepository productRepository) {

        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public CartItem addItemToCart(CartItem item) {

        if (item.getQuantity() == null || item.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        Cart cart = cartRepository.findById(item.getCart().getId())
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        if (!cart.getActive()) {
            throw new IllegalArgumentException("Only active carts");
        }

        Product product = productRepository.findById(item.getProduct().getId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        Optional<CartItem> existing =
                cartItemRepository.findByCartIdAndProductId(
                        cart.getId(), product.getId());

        if (existing.isPresent()) {
            CartItem e = existing.get();
            e.setQuantity(e.getQuantity() + item.getQuantity());
            return cartItemRepository.save(e);
        }

        item.setCart(cart);
        item.setProduct(product);
        return cartItemRepository.save(item);
    }

    public List<CartItem> getItemsForCart(Long cartId) {
        return cartItemRepository.findByCartId(cartId);
    }
}
