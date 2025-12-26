package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.CartItem;
import com.example.demo.model.DiscountApplication;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.service.DiscountService;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final CartItemRepository cartItemRepository;

    public DiscountServiceImpl(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public List<DiscountApplication> evaluateDiscounts(Long cartId) {

        List<CartItem> items = cartItemRepository.findByCartId(cartId);
        List<DiscountApplication> discounts = new ArrayList<>();

        for (CartItem ci : items) {
            if (ci.getQuantity() >= 2 && ci.getProduct() != null) {
                double amount = ci.getProduct().getPrice() * 0.10;
                discounts.add(new DiscountApplication("Bulk discount", amount));
            }
        }
        return discounts;
    }
}
