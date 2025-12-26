package com.example.demo.service.impl;

import com.example.demo.model.;
import com.example.demo.repository.;
import com.example.demo.service.DiscountService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DiscountServiceImpl implements DiscountService {
private final CartRepository cartRepository;
private final CartItemRepository cartItemRepository;
private final BundleRuleRepository bundleRuleRepository;
private final DiscountApplicationRepository discountApplicationRepository;

public DiscountServiceImpl(
        CartRepository cartRepository,
        CartItemRepository cartItemRepository,
        BundleRuleRepository bundleRuleRepository,
        DiscountApplicationRepository discountApplicationRepository
) {
    this.cartRepository = cartRepository;
    this.cartItemRepository = cartItemRepository;
    this.bundleRuleRepository = bundleRuleRepository;
    this.discountApplicationRepository = discountApplicationRepository;
}

@Override
public List<DiscountApplication> evaluateDiscounts(Long cartId) {

    Cart cart = cartRepository.findById(cartId).orElse(null);
    if (cart == null || !cart.getActive()) {
        return List.of();
    }

    List<CartItem> items = cartItemRepository.findByCartId(cartId);
    if (items.isEmpty()) {
        return List.of();
    }

    Set<Long> cartProducts = items.stream()
            .map(i -> i.getProduct().getId())
            .collect(Collectors.toSet());

    List<DiscountApplication> results = new ArrayList<>();

    for (BundleRule rule : bundleRuleRepository.findAll()) {

        if (!rule.getActive()) continue;

        Set<Long> required = Arrays.stream(rule.getRequiredProductIds().split(","))
                .map(String::trim)
                .map(Long::valueOf)
                .collect(Collectors.toSet());

        if (cartProducts.containsAll(required)) {
            DiscountApplication da = new DiscountApplication();
            da.setCart(cart);
            da.setBundleRule(rule);
            results.add(da);
        }
    }

    return results;
}

@Override
public List<DiscountApplication> getApplicationsForCart(Long cartId) {
    return discountApplicationRepository.findByCartId(cartId);
}
}