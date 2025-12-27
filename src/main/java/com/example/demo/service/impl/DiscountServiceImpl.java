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

        List<CartItem> items = itemRepo.findByCartId(cartId);
        Map<Long, Integer> quantities = new HashMap<>();

        for (CartItem item : items) {
            quantities.put(
                    item.getProduct().getId(),
                    quantities.getOrDefault(item.getProduct().getId(), 0) + item.getQuantity()
            );
        }

        List<DiscountApplication> applied = new ArrayList<>();

        for (BundleRule rule : ruleRepo.findAll()) {
            if (!rule.getActive()) continue;

            String[] ids = rule.getRequiredProductIds().split(",");
            boolean valid = true;

            for (String idStr : ids) {
                Long pid = Long.valueOf(idStr.trim());
                if (!quantities.containsKey(pid)) {
                    valid = false;
                    break;
                }
            }

            if (valid) {
                DiscountApplication da = new DiscountApplication();
                da.setCart(cart);
                da.setBundleRule(rule);
                da.setDiscountAmount(BigDecimal.ZERO);
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

    @Override
    public DiscountApplication getApplicationById(Long id) {
        return discountRepo.findById(id).orElse(null);
    }
}
