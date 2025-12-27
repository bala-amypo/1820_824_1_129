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

        if (!cart.getActive()) {
            throw new IllegalArgumentException("Cart is inactive");
        }

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        for (CartItem item : cartItemRepo.findAll()) {
            if (item.getCart().getId().equals(cartId)
                    && item.getProduct().getId().equals(productId)) {

                item.setQuantity(item.getQuantity() + quantity);
                return cartItemRepo.save(item);
            }
        }

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(quantity);
        return cartItemRepo.save(item);
    }

    @Override
    public CartItem addItemToCart(CartItem item) {
        return addItem(
                item.getCart().getId(),
                item.getProduct().getId(),
                item.getQuantity()
        );
    }

    @Override
    public CartItem updateItem(Long id, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        CartItem item = cartItemRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));

        item.setQuantity(quantity);
        return cartItemRepo.save(item);
    }

    @Override
    public List<CartItem> getItemsForCart(Long cartId) {
        return cartItemRepo.findAll().stream()
                .filter(i -> i.getCart().getId().equals(cartId))
                .toList();
    }

    @Override
    public void removeItem(Long id) {
        cartItemRepo.deleteById(id);
    }
}
