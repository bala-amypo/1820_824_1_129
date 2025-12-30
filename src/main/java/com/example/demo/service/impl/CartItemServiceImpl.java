package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.CartItemService;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    // ===== Controller-required methods =====

    @Override
    public CartItem addItem(Long cartId, Long productId, Integer quantity) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (!Boolean.TRUE.equals(cart.getActive())) {
            throw new RuntimeException("Cart is inactive");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItem> existing =
                cartItemRepository.findByCartAndProduct(cart, product);

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + quantity);
            return cartItemRepository.save(item);
        }

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(quantity);

        return cartItemRepository.save(item);
    }

    @Override
    public CartItem updateItem(Long cartItemId, Integer quantity) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));

        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }

    @Override
    public List<CartItem> getItemsForCart(Long cartId) {
        return cartItemRepository.findByCartId(cartId);
    }

    @Override
    public void removeItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    // ===== Test-required method =====

    @Override
    public CartItem addItemToCart(CartItem cartItem) {

        Cart cart = cartRepository.findById(cartItem.getCart().getId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (!Boolean.TRUE.equals(cart.getActive())) {
            throw new RuntimeException("Cart is inactive");
        }

        Optional<CartItem> existing =
                cartItemRepository.findByCartAndProduct(
                        cart, cartItem.getProduct());

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + cartItem.getQuantity());
            return cartItemRepository.save(item);
        }

        cartItem.setCart(cart);
        return cartItemRepository.save(cartItem);
    }
}
