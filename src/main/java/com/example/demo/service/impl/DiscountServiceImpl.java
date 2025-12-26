package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.CartItem;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.service.DiscountService;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final CartItemRepository cartItemRepository;

    public DiscountServiceImpl(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public double evaluateDiscounts(Long cartId) {

        List<CartItem> items = cartItemRepository.findByCartId(cartId);
        double discount = 0.0;

        for (CartItem ci : items) {
            if (ci.getQuantity() >= 2 && ci.getProduct() != null) {
                discount += ci.getProduct().getPrice() * 0.10;
            }
        }
        return discount;
    }
}
