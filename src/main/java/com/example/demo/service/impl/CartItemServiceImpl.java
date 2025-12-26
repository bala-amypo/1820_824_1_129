package com.example.demo.service.impl;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.CartItemService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

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
    public CartItem addItemToCart(CartItem item) {

        Cart cart = cartRepository.findById(item.getCart().getId())
                .orElseThrow();

        if (!cart.getActive()) {
            throw new IllegalArgumentException("Only active carts allowed");
        }

        if (item.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        Product product = productRepository.findById(item.getProduct().getId())
                .orElseThrow();

        Optional<CartItem> existing =
                cartItemRepository.findByCartIdAndProductId(
                        cart.getId(), product.getId());

        if (existing.isPresent()) {
            CartItem ci = existing.get();
            ci.setQuantity(ci.getQuantity() + item.getQuantity());
            return cartItemRepository.save(ci);
        }

        item.setCart(cart);
        item.setProduct(product);
        return cartItemRepository.save(item);
    }

    @Override
    public List<CartItem> getItemsForCart(Long cartId) {
        return cartItemRepository.findByCartId(cartId);
    }
}
