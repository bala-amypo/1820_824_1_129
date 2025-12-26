package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.CartItemService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartItemServiceImpl(
            CartRepository cartRepository,
            ProductRepository productRepository,
            CartItemRepository cartItemRepository) {

        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public CartItem addItem(Long cartId, Long productId, Integer quantity) {

        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(quantity);

        return cartItemRepository.save(item);
    }

    @Override
    public CartItem updateItem(Long id, Integer quantity) {

        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        CartItem item = cartItemRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("CartItem not found"));

        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }

    @Override
    public List<CartItem> getItemsForCart(Long cartId) {
        return cartItemRepository.findByCartId(cartId);
    }
    public CartItem addItemToCart(CartItem item) {

    if (item.getQuantity() == null || item.getQuantity() <= 0) {
        throw new IllegalArgumentException("Quantity must be positive");
    }

    Long cartId = item.getCart().getId();
    Long productId = item.getProduct().getId();

    CartItem existing = cartItemRepository
            .findByCartIdAndProductId(cartId, productId)
            .orElse(null);

    if (existing != null) {
        existing.setQuantity(existing.getQuantity() + item.getQuantity());
        return cartItemRepository.save(existing);
    }

    return cartItemRepository.save(item);
}


    @Override
    public void removeItem(Long id) {
        CartItem item = cartItemRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("CartItem not found"));
        cartItemRepository.delete(item);
    }
}
