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

    private final CartItemRepository itemRepo;
    private final CartRepository cartRepo;
    private final ProductRepository productRepo;

    public CartItemServiceImpl(
            CartItemRepository itemRepo,
            CartRepository cartRepo,
            ProductRepository productRepo
    ) {
        this.itemRepo = itemRepo;
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

        for (CartItem ci : itemRepo.findAll()) {
            if (ci.getCart().getId().equals(cartId)
                    && ci.getProduct().getId().equals(productId)) {

                ci.setQuantity(ci.getQuantity() + quantity);
                return itemRepo.save(ci);
            }
        }

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(quantity);
        return itemRepo.save(item);
    }

    @Override
    public CartItem updateItem(Long itemId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        CartItem item = itemRepo.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));

        item.setQuantity(quantity);
        return itemRepo.save(item);
    }

    @Override
    public void removeItem(Long itemId) {
        itemRepo.deleteById(itemId);
    }

    @Override
    public List<CartItem> getItemsForCart(Long cartId) {
        return itemRepo.findAll().stream()
                .filter(i -> i.getCart().getId().equals(cartId))
                .toList();
    }
}
