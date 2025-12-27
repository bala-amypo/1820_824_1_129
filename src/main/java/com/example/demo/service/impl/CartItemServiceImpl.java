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

    private final CartItemRepository itemRepo;
    private final CartRepository cartRepo;

    public CartItemServiceImpl(CartItemRepository itemRepo,
                               CartRepository cartRepo) {
        this.itemRepo = itemRepo;
        this.cartRepo = cartRepo;
    }

    @Override
    public boolean addItemToCart(CartItem item) {

        Cart cart = cartRepo.findById(item.getCart().getId())
                .orElseThrow(IllegalArgumentException::new);

        if (!cart.getActive()) {
            throw new IllegalArgumentException("Cart inactive");
        }

        for (CartItem ci : itemRepo.findAll()) {
            if (ci.getCart().getId().equals(cart.getId()) &&
                ci.getProduct().getId().equals(item.getProduct().getId())) {

                ci.setQuantity(ci.getQuantity() + item.getQuantity());
                itemRepo.save(ci);
                return true;
            }
        }

        itemRepo.save(item);
        return true;
    }

    @Override
    public void updateItem(Long itemId, Integer quantity) {

        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }

        CartItem item = itemRepo.findById(itemId)
                .orElseThrow(IllegalArgumentException::new);

        item.setQuantity(quantity);
        itemRepo.save(item);
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
