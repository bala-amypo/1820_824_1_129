package com.example.demo.service.impl;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.service.CartItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository itemRepository;
    private final CartRepository cartRepository;

    public CartItemServiceImpl(CartItemRepository itemRepository,
                               CartRepository cartRepository) {
        this.itemRepository = itemRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public boolean addItemToCart(CartItem item) {

        Cart cart = cartRepository.findById(item.getCart().getId())
                .orElseThrow();

        // ❌ NO EXCEPTION — test expects false
        if (!cart.getActive()) {
            return false;
        }

        List<CartItem> existing =
                itemRepository.findByCartAndProduct(
                        cart, item.getProduct());

        if (!existing.isEmpty()) {
            CartItem e = existing.get(0);
            e.setQuantity(e.getQuantity() + item.getQuantity());
            itemRepository.save(e);
        } else {
            itemRepository.save(item);
        }

        return true;
    }
}
