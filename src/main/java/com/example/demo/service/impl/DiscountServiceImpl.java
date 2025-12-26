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
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
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

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        if (Boolean.FALSE.equals(cart.getActive())) {
            throw new IllegalStateException("Cart is inactive");
        }

        List<CartItem> items = cartItemRepository.findByCartId(cartId);

        if (items.isEmpty()) {
            return List.of();
        }

        Set<Long> productIds = items.stream()
                .filter(i -> i.getProduct() != null)
                .map(i -> i.getProduct().getId())
                .collect(Collectors.toSet());

        List<DiscountApplication> results = new ArrayList<>();

        for (BundleRule rule : bundleRuleRepository.findAll()) {

            if (Boolean.FALSE.equals(rule.getActive())) {
                continue;
            }

            Set<Long> required = rule.getRequiredProductIdSet();
            if (required == null || required.isEmpty()) {
                continue;
            }

            if (productIds.containsAll(required)) {
                DiscountApplication app = new DiscountApplication();
                app.setCart(cart);
                app.setBundleRule(rule);

                results.add(discountApplicationRepository.save(app));
            }
        }

        return results;
    }

    @Override
    public List<DiscountApplication> getApplicationsForCart(Long cartId) {
        if (!cartRepository.existsById(cartId)) {
            throw new IllegalArgumentException("Cart not found");
        }
        return discountApplicationRepository.findByCartId(cartId);
    }

    @Override
    public DiscountApplication getApplicationById(Long id) {
        return discountApplicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Discount application not found"));
    }
}
