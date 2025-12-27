package com.example.demo.service.impl;

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

    private final CartItemRepository itemRepo;
    private final CartRepository cartRepo;
    private final ProductRepository productRepo;

    public CartItemServiceImpl(CartItemRepository itemRepo,
                               CartRepository cartRepo,
                               ProductRepository productRepo) {
        this.itemRepo = itemRepo;
        this.cartRepo = cartRepo;
        this.productRepo = productRepo;
    }

    public CartItem addItemToCart(CartItem item) {

        if (item.getQuantity() == null || item.getQuantity() <= 0) {
            return null; // REQUIRED by test
        }

        Cart cart = cartRepo.findById(item.getCart().getId()).orElse(null);
        if (cart == null || !cart.getActive()) {
            return null; // REQUIRED by test
        }

        for (CartItem existing : itemRepo.findAll()) {
            if (existing.getCart().getId().equals(cart.getId())
                    && existing.getProduct().getId().equals(item.getProduct().getId())) {

                existing.setQuantity(existing.getQuantity() + item.getQuantity());
                itemRepo.save(existing);

                // IMPORTANT: re-fetch so quantity reflects DB state
                return itemRepo.findById(existing.getId()).orElse(existing);
            }
        }

        return itemRepo.save(item);
    }

    @Override
    public CartItem addItem(Long cartId, Long productId, Integer quantity) {

        if (quantity == null || quantity <= 0) {
            return null;
        }

        Cart cart = cartRepo.findById(cartId).orElse(null);
        if (cart == null || !cart.getActive()) {
            return null;
        }

        Product product = productRepo.findById(productId).orElse(null);
        if (product == null) {
            return null;
        }

        for (CartItem existing : itemRepo.findAll()) {
            if (existing.getCart().getId().equals(cartId)
                    && existing.getProduct().getId().equals(productId)) {

                existing.setQuantity(existing.getQuantity() + quantity);
                itemRepo.save(existing);
                return itemRepo.findById(existing.getId()).orElse(existing);
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
            return null;
        }

        CartItem item = itemRepo.findById(itemId).orElse(null);
        if (item == null) return null;

        item.setQuantity(quantity);
        return itemRepo.save(item);
    }

    @Override
    public void removeItem(Long itemId) {
        itemRepo.deleteById(itemId);
    }

    @Override
    public List<CartItem> getItemsForCart(Long cartId) {
        return itemRepo.findAll()
                .stream()
                .filter(i -> i.getCart().getId().equals(cartId))
                .toList();
    }
}
