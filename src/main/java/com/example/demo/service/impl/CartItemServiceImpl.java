package com.example.demo.service.impl;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.CartItemService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartItemServiceImpl(CartItemRepository cartItemRepository,
                               CartRepository cartRepository,
                               ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Override
    public CartItem addItemToCart(CartItem item) {
        Long cartId = item.getCart() != null ? item.getCart().getId() : null;
        Long productId = item.getProduct() != null ? item.getProduct().getId() : null;

        if (cartId == null) throw new IllegalArgumentException("Cart is required");
        if (productId == null) throw new IllegalArgumentException("Product is required");
        if (item.getQuantity() == null || item.getQuantity() <= 0) throw new IllegalArgumentException("Quantity must be positive");

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));
        if (!Boolean.TRUE.equals(cart.getActive())) throw new IllegalArgumentException("Items can be added only to active carts");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        if (!Boolean.TRUE.equals(product.getActive())) throw new IllegalArgumentException("Product is inactive");

        return cartItemRepository.findByCartIdAndProductId(cartId, productId)
                .map(existing -> {
                    existing.setQuantity(existing.getQuantity() + item.getQuantity());
                    return cartItemRepository.save(existing);
                })
                .orElseGet(() -> {
                    item.setCart(cart);
                    item.setProduct(product);
                    return cartItemRepository.save(item);
                });
    }

    @Override
    public List<CartItem> getItemsForCart(Long cartId) {
        return cartItemRepository.findByCartId(cartId);
    }
}
