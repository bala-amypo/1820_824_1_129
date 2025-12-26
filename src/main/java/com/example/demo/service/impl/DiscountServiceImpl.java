package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
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
        if (cart == null || !cart.isActive()) {
            return List.of();
        }

        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);
        if (cartItems.isEmpty()) {
            return List.of();
        }

        Set<Long> cartProductIds = cartItems.stream()
                .map(ci -> ci.getProduct().getId())
                .collect(Collectors.toSet());

        List<DiscountApplication> applied = new ArrayList<>();

        for (BundleRule rule : bundleRuleRepository.findAll()) {

            if (rule.getRequiredProductIds() == null || rule.getRequiredProductIds().isBlank()) {
                continue;
            }

            Set<Long> requiredIds = Arrays.stream(rule.getRequiredProductIds().split(","))
                    .map(String::trim)
                    .map(Long::valueOf)
                    .collect(Collectors.toSet());

            if (cartProductIds.containsAll(requiredIds)) {
                DiscountApplication da = new DiscountApplication();
                da.setCart(cart);
                da.setRule(rule);
                applied.add(da);
            }
        }

        return applied;
    }
}
