package com.example.demo.service.impl;

import com.example.demo.model.BundleRule;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.DiscountApplication;
import com.example.demo.repository.BundleRuleRepository;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.DiscountApplicationRepository;
import com.example.demo.service.DiscountService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
    Set<Long> productIds = items.stream()
            .map(i -> i.getProduct().getId())
            .collect(Collectors.toSet());

    List<DiscountApplication> results = new ArrayList<>();

    for (BundleRule rule : bundleRuleRepository.findAll()) {
        if (!rule.getActive()) continue;

        Set<Long> required = rule.getRequiredProductIdSet();
        if (productIds.containsAll(required)) {
            DiscountApplication app = new DiscountApplication();
            app.setCart(cart);
            app.setBundleRule(rule);
            results.add(app);
        }
    }
    return results;
}

@Override
public List<DiscountApplication> getApplicationsForCart(Long cartId) {
    return discountApplicationRepository.findByCartId(cartId);
}

@Override
public DiscountApplication getApplicationById(Long id) {
    return discountApplicationRepository.findById(id).orElse(null);
}
}