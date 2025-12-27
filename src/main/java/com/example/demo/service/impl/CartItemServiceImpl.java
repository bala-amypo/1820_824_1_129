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
    public CartItem addItemToCart(CartItem item) {
        if (item == null) {
            throw new IllegalArgumentException("CartItem cannot be null");
        }

        if (item.getQuantity() == null || item.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        Cart cart = cartRepo.findById(item.getCart().getId())
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        if (!cart.getActive()) {
            throw new IllegalArgumentException("Cart is inactive");
        }

        Product product = productRepo.findById(item.getProduct().getId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        Optional<CartItem> existing =
                cartItemRepo.findAll().stream()
                        .filter(ci ->
                                ci.getCart().getId().equals(cart.getId()) &&
                                ci.getProduct().getId().equals(product.getId()))
                        .findFirst();

        if (existing.isPresent()) {
            CartItem existingItem = existing.get();
            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
            return cartItemRepo.save(existingItem);
        }

        item.setCart(cart);
        item.setProduct(product);
        return cartItemRepo.save(item);
    }

    @Override
    public CartItem addItem(Long cartId, Long productId, Integer quantity) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(quantity);

        return addItemToCart(item);
    }

    // ✅ THIS FIXES YOUR CURRENT ERROR
    @Override
    public CartItem updateItem(Long itemId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        CartItem item = cartItemRepo.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));

        item.setQuantity(quantity);
        return cartItemRepo.save(item);
    }

    @Override
    public List<CartItem> getItemsForCart(Long cartId) {
        return cartItemRepo.findAll().stream()
                .filter(ci -> ci.getCart().getId().equals(cartId))
                .toList();
    }

    @Override
    public void removeItem(Long itemId) {
        cartItemRepo.deleteById(itemId);
    }
}
