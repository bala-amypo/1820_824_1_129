package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.DiscountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

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
        if (cart == null || !cart.getActive()) {
            return List.of();
        }

        List<CartItem> items = itemRepo.findAll().stream()
                .filter(i -> i.getCart().getId().equals(cartId))
                .toList();

        Map<Long, Integer> quantityByProduct = new HashMap<>();
        for (CartItem i : items) {
            quantityByProduct.merge(
                    i.getProduct().getId(),
                    i.getQuantity(),
                    Integer::sum
            );
        }

        List<DiscountApplication> applied = new ArrayList<>();

        for (BundleRule rule : ruleRepo.findAll()) {
            if (!rule.getActive()) continue;

            String[] requiredIds = rule.getRequiredProductIds().split(",");
            int totalQty = 0;

            for (String pid : requiredIds) {
                Long id = Long.parseLong(pid.trim());
                totalQty += quantityByProduct.getOrDefault(id, 0);
            }

            if (totalQty >= rule.getMinQuantity()) {
                DiscountApplication da = new DiscountApplication();
                da.setCart(cart);
                da.setBundleRule(rule);
                da.setDiscountAmount(BigDecimal.ONE);
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
