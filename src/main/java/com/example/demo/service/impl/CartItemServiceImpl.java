package com.example.demo.service.impl;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.service.CartItemService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepo;
    private final CartRepository cartRepo;

    public CartItemServiceImpl(
            CartItemRepository cartItemRepo,
            CartRepository cartRepo
    ) {
        this.cartItemRepo = cartItemRepo;
        this.cartRepo = cartRepo;
    }

    // ✅ REQUIRED BY TESTS
    @Override
    public CartItem addItemToCart(CartItem item) {
        if (item.getQuantity() == null || item.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        Cart cart = cartRepo.findById(item.getCart().getId())
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        if (!cart.getActive()) {
            throw new IllegalArgumentException("Cart is inactive");
        }

        // aggregate quantity if same product exists
        for (CartItem existing : cartItemRepo.findAll()) {
            if (existing.getCart().getId().equals(item.getCart().getId())
                    && existing.getProduct().getId().equals(item.getProduct().getId())) {

                existing.setQuantity(existing.getQuantity() + item.getQuantity());
                return cartItemRepo.save(existing);
            }
        }

        return cartItemRepo.save(item);
    }

    // ✅ REQUIRED BY CONTROLLER
    @Override
    public CartItem updateItem(Long itemId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        CartItem item = cartItemRepo.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("CartItem not found"));

        item.setQuantity(quantity);
        return cartItemRepo.save(item);
    }

    // ✅ REQUIRED BY CONTROLLER
    @Override
    public List<CartItem> getItemsForCart(Long cartId) {
        return cartItemRepo.findAll().stream()
                .filter(i -> i.getCart().getId().equals(cartId))
                .toList();
    }

    @Override
    public void removeItem(Long itemId) {
        cartItemRepo.deleteById(itemId);
    }
}
