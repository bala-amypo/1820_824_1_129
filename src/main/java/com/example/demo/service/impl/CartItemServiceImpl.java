package com.example.demo.service.impl;
import org.springframework.stereotype.Service;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.CartItemService;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepo;
    private final CartRepository cartRepo;
    private final ProductRepository productRepo;

    public CartItemServiceImpl(
            CartItemRepository cartItemRepo,
            CartRepository cartRepo,
            ProductRepository productRepo) {
        this.cartItemRepo = cartItemRepo;
        this.cartRepo = cartRepo;
        this.productRepo = productRepo;
    }

    @Override
    public CartItem addItem(Long cartId, Long productId, Integer quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        return cartItemRepo.findByCartIdAndProductId(cartId, productId)
                .map(item -> {
                    item.setQuantity(item.getQuantity() + quantity);
                    return cartItemRepo.save(item);
                })
                .orElseGet(() -> {
                    CartItem item = new CartItem();
                    item.setCart(cart);
                    item.setProduct(product);
                    item.setQuantity(quantity);
                    return cartItemRepo.save(item);
                });
    }

    @Override
    public CartItem updateItem(Long id, Integer quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        CartItem item = cartItemRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CartItem not found"));
        item.setQuantity(quantity);
        return cartItemRepo.save(item);
    }

    @Override
    public List<CartItem> getItemsForCart(Long cartId) {
        return cartItemRepo.findByCartId(cartId);
    }

    @Override
    public void removeItem(Long id) {
        cartItemRepo.deleteById(id);
    }
}
