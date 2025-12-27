package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.BundleRuleRepository;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.DiscountApplicationRepository;
import com.example.demo.service.DiscountService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final BundleRuleRepository ruleRepo;
    private final CartItemRepository itemRepo;
    private final DiscountApplicationRepository discountRepo;

    public DiscountServiceImpl(BundleRuleRepository ruleRepo,
                               CartItemRepository itemRepo,
                               DiscountApplicationRepository discountRepo) {
        this.ruleRepo = ruleRepo;
        this.itemRepo = itemRepo;
        this.discountRepo = discountRepo;
    }

    @Override
    public List<DiscountApplication> evaluateDiscounts(Cart cart) {

        if (!cart.getActive()) return List.of();

        List<CartItem> items = itemRepo.findByCart(cart);
        Set<Long> productIds = new HashSet<>();

        for (CartItem item : items) {
            productIds.add(item.getProduct().getId());
        }

        List<DiscountApplication> applied = new ArrayList<>();

        for (BundleRule rule : ruleRepo.findAll()) {

            if (!rule.getActive()) continue;

            String[] required = rule.getRequiredProductIds().split(",");

            boolean matches = true;
            for (String id : required) {
                if (!productIds.contains(Long.parseLong(id.trim()))) {
                    matches = false;
                    break;
                }
            }

            if (matches) {
                DiscountApplication da = new DiscountApplication();
                da.setCart(cart);
                da.setRule(rule);
                applied.add(discountRepo.save(da));
            }
        }

        return applied;
    }
}
