package com.example.demo.service.impl;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.service.CartItemService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;

    public CartItemServiceImpl(
            CartItemRepository cartItemRepository,
            CartRepository cartRepository
    ) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
    }

    /**
     * ✅ REQUIRED BY TESTS
     * Convenience method that delegates to addItem(...)
     */
    public CartItem addItemToCart(CartItem item) {

        if (item == null || item.getCart() == null || item.getProduct() == null) {
            throw new IllegalArgumentException("Cart, product and quantity must be provided");
        }

        return addItem(
                item.getCart().getId(),
                item.getProduct().getId(),
                item.getQuantity()
        );
    }

    /**
     * ✅ REQUIRED BY INTERFACE
     */
    @Override
    public CartItem addItem(Long cartId, Long productId, Integer quantity) {

        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        if (Boolean.FALSE.equals(cart.getActive())) {
            throw new IllegalStateException("Cart is inactive");
        }

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setQuantity(quantity);

        Product product = new Product();
        product.setId(productId);
        item.setProduct(product);

        return cartItemRepository.save(item);
    }

    @Override
    public void removeItem(Long itemId) {
        if (!cartItemRepository.existsById(itemId)) {
            throw new IllegalArgumentException("Cart item not found");
        }
        cartItemRepository.deleteById(itemId);
    }

    @Override
    public CartItem updateItem(Long itemId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));

        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }

    @Override
    public List<CartItem> getItemsForCart(Long cartId) {
        if (!cartRepository.existsById(cartId)) {
            throw new IllegalArgumentException("Cart not found");
        }
        return cartItemRepository.findByCartId(cartId);
    }
}
