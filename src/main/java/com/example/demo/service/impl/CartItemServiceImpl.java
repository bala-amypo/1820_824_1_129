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
        Cart cart = cartRepository.findById(item.getCart().getId())
            .orElseThrow(() -> new EntityNotFoundException("Cart not found"));
        
        Product product = productRepository.findById(item.getProduct().getId())
            .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        
        if (!cart.getActive()) {
            throw new IllegalArgumentException("Items can only be added to active carts");
        }
        
        if (!product.getActive()) {
            throw new IllegalArgumentException("Product is not active");
        }
        
        if (item.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        
        return cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId())
            .map(existing -> {
                existing.setQuantity(existing.getQuantity() + item.getQuantity());
                return cartItemRepository.save(existing);
            })
            .orElseGet(() -> cartItemRepository.save(item));
    }
    
    @Override
    public List<CartItem> getItemsForCart(Long cartId) {
        return cartItemRepository.findByCartId(cartId);
    }
}