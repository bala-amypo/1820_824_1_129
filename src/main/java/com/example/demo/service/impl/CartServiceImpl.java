package com.example.demo.service.impl;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.CartItemService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private ProductRepository productRepository;

    @Override
    public CartItem addItemToCart(CartItem item) {
        Cart cart = cartRepository.findById(item.getCart().getId())
            .orElseThrow(() -> new EntityNotFoundException("Cart not found"));
        
        if (!cart.getActive()) {
            throw new IllegalArgumentException("Cannot add items to inactive carts");
        }
        
        Product product = productRepository.findById(item.getProduct().getId())
            .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        
        if (!product.getActive()) {
            throw new IllegalArgumentException("Cannot add inactive products to cart");
        }
        
        if (item.getQuantity() == null || item.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        
        return cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId())
            .map(existingItem -> {
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                return cartItemRepository.save(existingItem);
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
