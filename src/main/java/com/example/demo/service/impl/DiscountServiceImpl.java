package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.DiscountService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final CartItemRepository cartItemRepo;
    private final BundleRuleRepository ruleRepo;
    private final DiscountApplicationRepository discountRepo;

    public DiscountServiceImpl(CartItemRepository cartItemRepo,
                               BundleRuleRepository ruleRepo,
                               DiscountApplicationRepository discountRepo) {
        this.cartItemRepo = cartItemRepo;
        this.ruleRepo = ruleRepo;
        this.discountRepo = discountRepo;
    }

    @Override
    public List<DiscountApplication> evaluateDiscounts(Cart cart) {

        if (!cart.getActive()) {
            return List.of();
        }

        List<CartItem> items = cartItemRepo.findByCart(cart);
        Set<Long> productIds = items.stream()
                .map(i -> i.getProduct().getId())
                .collect(Collectors.toSet());

        List<DiscountApplication> applied = new ArrayList<>();

        for (BundleRule rule : ruleRepo.findAll()) {

            if (!rule.getActive()) continue;

            Set<Long> required = Arrays.stream(
                    rule.getRequiredProductIds().split(","))
                    .map(String::trim)
                    .map(Long::valueOf)
                    .collect(Collectors.toSet());

            if (productIds.containsAll(required)) {
                DiscountApplication da = new DiscountApplication();
                da.setCart(cart);
                da.setRule(rule);
                discountRepo.save(da);
                applied.add(da);
            }
        }

        return applied;
    }
}
