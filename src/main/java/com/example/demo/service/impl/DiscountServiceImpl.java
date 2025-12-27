package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.DiscountService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final CartItemRepository itemRepo;
    private final BundleRuleRepository ruleRepo;
    private final DiscountApplicationRepository discountRepo;
    private final CartRepository cartRepo;

    public DiscountServiceImpl(CartItemRepository itemRepo,
                               BundleRuleRepository ruleRepo,
                               DiscountApplicationRepository discountRepo,
                               CartRepository cartRepo) {
        this.itemRepo = itemRepo;
        this.ruleRepo = ruleRepo;
        this.discountRepo = discountRepo;
        this.cartRepo = cartRepo;
    }

    @Override
    public List<DiscountApplication> evaluateDiscounts(Cart cart) {

        if (!cart.getActive()) {
            return List.of();
        }

        // clear old discounts for cart
        discountRepo.findAll()
                .stream()
                .filter(d -> d.getCart().getId().equals(cart.getId()))
                .forEach(discountRepo::delete);

        Set<Long> productIds = itemRepo.findAll()
                .stream()
                .filter(i -> i.getCart().getId().equals(cart.getId()))
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
                discountRepo.save(da);
                result.add(da);
            }
        }

        return result;
    }

    @Override
    public DiscountApplication getApplicationById(Long id) {
        return discountRepo.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public List<DiscountApplication> getApplicationsForCart(Long cartId) {
        return discountRepo.findAll()
                .stream()
                .filter(d -> d.getCart().getId().equals(cartId))
                .toList();
    }
}
