package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.DiscountService;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.*;

public class DiscountServiceImpl implements DiscountService {

    private final DiscountApplicationRepository discountRepository;
    private final BundleRuleRepository bundleRuleRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public DiscountServiceImpl(
            DiscountApplicationRepository discountRepository,
            BundleRuleRepository bundleRuleRepository,
            CartRepository cartRepository,
            CartItemRepository cartItemRepository) {

        this.discountRepository = discountRepository;
        this.bundleRuleRepository = bundleRuleRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public List<DiscountApplication> evaluateDiscounts(Long cartId) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        List<CartItem> items = cartItemRepository.findByCartId(cartId);
        List<BundleRule> rules = bundleRuleRepository.findByActiveTrue();

        List<DiscountApplication> results = new ArrayList<>();

        for (BundleRule rule : rules) {

            Set<Long> requiredIds = new HashSet<>();
            for (String id : rule.getRequiredProductIds().split(",")) {
                requiredIds.add(Long.parseLong(id.trim()));
            }

            Set<Long> foundIds = new HashSet<>();
            BigDecimal total = BigDecimal.ZERO;

            for (CartItem item : items) {
                Long pid = item.getProduct().getId();
                if (requiredIds.contains(pid)) {
                    foundIds.add(pid);
                    total = total.add(
                        item.getProduct().getPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity()))
                    );
                }
            }

            if (foundIds.containsAll(requiredIds)) {

                BigDecimal discount = total
                        .multiply(BigDecimal.valueOf(rule.getDiscountPercentage()))
                        .divide(BigDecimal.valueOf(100));

                DiscountApplication app = new DiscountApplication();
                app.setCart(cart);
                app.setBundleRule(rule);
                app.setDiscountAmount(discount);

                results.add(discountRepository.save(app));
            }
        }
        return results;
    }

    @Override
    public DiscountApplication getApplicationById(Long id) {
        return discountRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("DiscountApplication not found"));
    }

    @Override
    public List<DiscountApplication> getApplicationsForCart(Long cartId) {
        return discountRepository.findByCartId(cartId);
    }
}
