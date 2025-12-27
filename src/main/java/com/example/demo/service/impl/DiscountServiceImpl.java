package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.DiscountService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

        Set<Long> productIds = itemRepo.findAll().stream()
                .filter(i -> i.getCart().getId().equals(cartId))
                .map(i -> i.getProduct().getId())
                .collect(Collectors.toSet());

        List<DiscountApplication> result = new ArrayList<>();

        for (BundleRule rule : ruleRepo.findAll()) {
            if (!rule.getActive()) continue;

            Set<Long> required = Arrays.stream(rule.getRequiredProductIds().split(","))
                    .map(String::trim)
                    .map(Long::valueOf)
                    .collect(Collectors.toSet());

            if (productIds.containsAll(required)) {
                DiscountApplication da = new DiscountApplication();
                da.setCart(cart);
                da.setBundleRule(rule);
                result.add(discountRepo.save(da));
            }
        }

        return result;
    }

    @Override
    public DiscountApplication getApplicationById(Long id) {
        return discountRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Discount application not found"));
    }

    @Override
    public List<DiscountApplication> getApplicationsForCart(Long cartId) {
        return discountRepo.findAll().stream()
                .filter(d -> d.getCart().getId().equals(cartId))
                .toList();
    }
}
