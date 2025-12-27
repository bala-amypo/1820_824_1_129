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

import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepo;
    private final CartRepository cartRepo;
    private final ProductRepository productRepo;

    public CartItemServiceImpl(
            CartItemRepository cartItemRepo,
            CartRepository cartRepo,
            ProductRepository productRepo
    ) {
        this.cartItemRepo = cartItemRepo;
        this.cartRepo = cartRepo;
        this.productRepo = productRepo;
    }

    @Override
    public CartItem addItem(Long cartId, Long productId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        if (!cart.isActive()) {
            throw new IllegalArgumentException("Cart is inactive");
        }

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        Optional<CartItem> existing =
                cartItemRepo.findAll().stream()
                        .filter(ci ->
                                ci.getCart().getId().equals(cartId) &&
                                ci.getProduct().getId().equals(productId))
                        .findFirst();

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + quantity);
            return cartItemRepo.save(item);
        }

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(quantity);

        return cartItemRepo.save(item);
    }

    @Override
    public void removeItem(Long itemId) {
        cartItemRepo.deleteById(itemId);
    }
}
