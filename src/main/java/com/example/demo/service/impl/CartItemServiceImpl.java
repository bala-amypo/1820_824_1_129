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

    /* =========================================================
       REQUIRED BY TESTS
       ========================================================= */
    public CartItem addItemToCart(CartItem item) {

        if (item == null || item.getQuantity() == null || item.getQuantity() <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }

        Cart cart = cartRepo.findById(item.getCart().getId())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        if (!cart.getActive()) {
            throw new IllegalArgumentException("Cart inactive");
        }

        for (CartItem existing : itemRepo.findAll()) {
            if (existing.getCart().getId().equals(cart.getId())
                    && existing.getProduct().getId().equals(item.getProduct().getId())) {

                existing.setQuantity(existing.getQuantity() + item.getQuantity());
                return itemRepo.save(existing);
            }
        }

        return itemRepo.save(item);
    }

    /* =========================================================
       REQUIRED BY INTERFACE
       ========================================================= */
    @Override
    public CartItem addItem(Long cartId, Long productId, Integer quantity) {

        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }

        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        if (!cart.getActive()) {
            throw new IllegalArgumentException("Cart inactive");
        }

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        for (CartItem existing : itemRepo.findAll()) {
            if (existing.getCart().getId().equals(cartId)
                    && existing.getProduct().getId().equals(productId)) {

                existing.setQuantity(existing.getQuantity() + quantity);
                return itemRepo.save(existing);
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
            throw new IllegalArgumentException("Invalid quantity");
        }

        CartItem item = itemRepo.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

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
