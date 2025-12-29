package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class DiscountServiceImpl {

    private final DiscountApplicationRepository discountRepo;
    private final BundleRuleRepository ruleRepo;
    private final CartRepository cartRepo;
    private final CartItemRepository itemRepo;

    public DiscountServiceImpl(
            DiscountApplicationRepository discountRepo,
            BundleRuleRepository ruleRepo,
            CartRepository cartRepo,
            CartItemRepository itemRepo) {

        this.discountRepo = discountRepo;
        this.ruleRepo = ruleRepo;
        this.cartRepo = cartRepo;
        this.itemRepo = itemRepo;
    }

    public List<DiscountApplication> evaluateDiscounts(Long cartId) {

        Cart cart = cartRepo.findById(cartId).orElse(null);
        if (cart == null || !cart.getActive()) return List.of();

        List<CartItem> items = itemRepo.findByCartId(cartId);
        if (items.isEmpty()) return List.of();

        discountRepo.deleteByCartId(cartId);

        Set<Long> productIds = new HashSet<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem ci : items) {
            productIds.add(ci.getProduct().getId());
            total = total.add(
                    ci.getProduct().getPrice()
                            .multiply(BigDecimal.valueOf(ci.getQuantity()))
            );
        }

        List<DiscountApplication> result = new ArrayList<>();

        for (BundleRule rule : ruleRepo.findByActiveTrue()) {
            Set<Long> required = new HashSet<>();
            for (String s : rule.getRequiredProductIds().split(",")) {
                required.add(Long.parseLong(s.trim()));
            }

            if (productIds.containsAll(required)) {
                DiscountApplication da = new DiscountApplication();
                da.setCart(cart);
                da.setBundleRule(rule);
                da.setAppliedAt(LocalDateTime.now());
                da.setDiscountAmount(
                        total.multiply(
                                BigDecimal.valueOf(rule.getDiscountPercentage() / 100))
                );
                result.add(discountRepo.save(da));
            }
        }
        return result;
    }
}
