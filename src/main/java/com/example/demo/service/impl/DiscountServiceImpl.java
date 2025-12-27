package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.DiscountService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final CartRepository cartRepo;
    private final CartItemRepository itemRepo;
    private final BundleRuleRepository ruleRepo;
    private final DiscountApplicationRepository discountRepo;

    public DiscountServiceImpl(
            CartRepository cartRepo,
            CartItemRepository itemRepo,
            BundleRuleRepository ruleRepo,
            DiscountApplicationRepository discountRepo
    ) {
        this.cartRepo = cartRepo;
        this.itemRepo = itemRepo;
        this.ruleRepo = ruleRepo;
        this.discountRepo = discountRepo;
    }

    @Override
    public List<DiscountApplication> evaluateDiscounts(Long cartId) {
        Cart cart = cartRepo.findById(cartId).orElse(null);
        if (cart == null || !cart.isActive()) {
            return List.of();
        }

        List<CartItem> items = itemRepo.findAll();
        List<BundleRule> rules = ruleRepo.findAll();
        List<DiscountApplication> applied = new ArrayList<>();

        for (BundleRule rule : rules) {
            boolean match = items.stream()
                    .filter(i -> i.getCart().getId().equals(cartId))
                    .count() >= rule.getMinQuantity();

            if (match) {
                DiscountApplication da = new DiscountApplication();
                da.setCart(cart);
                da.setBundleRule(rule);
                applied.add(discountRepo.save(da));
            }
        }

        return applied;
    }

    @Override
    public List<DiscountApplication> getApplicationsForCart(Long cartId) {
        return discountRepo.findAll().stream()
                .filter(d -> d.getCart().getId().equals(cartId))
                .toList();
    }
}
