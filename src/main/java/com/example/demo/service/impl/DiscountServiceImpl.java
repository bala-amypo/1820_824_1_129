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
import com.example.demo.exception.ResourceNotFoundException;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
            DiscountApplicationRepository discountApplicationRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.bundleRuleRepository = bundleRuleRepository;
        this.discountApplicationRepository = discountApplicationRepository;
    }

    @Override
    public List<DiscountApplication> evaluateDiscounts(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        List<CartItem> items = cartItemRepository.findByCart(cart);
        Set<Long> productIds = items.stream()
                .map(i -> i.getProduct().getId())
                .collect(Collectors.toSet());

        List<BundleRule> rules = bundleRuleRepository.findByActiveTrue();

        return rules.stream()
                .filter(rule -> containsAllProducts(rule.getRequiredProductIds(), productIds))
                .map(rule -> applyDiscount(cart, rule, items))
                .toList();
    }

    private boolean containsAllProducts(String csv, Set<Long> productIds) {
        for (String id : csv.split(",")) {
            if (!productIds.contains(Long.parseLong(id.trim()))) {
                return false;
            }
        }
        return true;
    }

    private DiscountApplication applyDiscount(Cart cart, BundleRule rule, List<CartItem> items) {
        BigDecimal total = items.stream()
                .map(i -> i.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal discount = total
                .multiply(BigDecimal.valueOf(rule.getDiscountPercentage()))
                .divide(BigDecimal.valueOf(100));

        DiscountApplication da = new DiscountApplication();
        da.setCart(cart);
        da.setBundleRule(rule);
        da.setDiscountAmount(discount);

        return discountApplicationRepository.save(da);
    }

    @Override
    public DiscountApplication getApplicationById(Long id) {
        return discountApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DiscountApplication not found"));
    }

    @Override
    public List<DiscountApplication> getApplicationsForCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        return discountApplicationRepository.findByCart(cart);
    }
}
