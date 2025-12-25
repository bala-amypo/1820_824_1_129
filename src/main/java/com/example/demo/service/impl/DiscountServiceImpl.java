package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.DiscountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final BundleRuleRepository bundleRuleRepository;
    private final DiscountApplicationRepository discountApplicationRepository;

    public DiscountServiceImpl(CartRepository cartRepository,
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
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null || !Boolean.TRUE.equals(cart.getActive())) return Collections.emptyList();

        List<CartItem> items = cartItemRepository.findByCartId(cartId);
        if (items == null || items.isEmpty()) return Collections.emptyList();

        Set<Long> cartProductIds = items.stream()
                .filter(ci -> ci.getProduct() != null && ci.getProduct().getId() != null)
                .map(ci -> ci.getProduct().getId())
                .collect(Collectors.toSet());

        discountApplicationRepository.deleteByCartId(cartId);

        List<BundleRule> rules = bundleRuleRepository.findByActiveTrue();
        List<DiscountApplication> result = new ArrayList<>();

        for (BundleRule rule : rules) {
            if (!Boolean.TRUE.equals(rule.getActive())) continue;

            Set<Long> required = parseCsvIds(rule.getRequiredProductIds());
            if (required.isEmpty()) continue;

            if (cartProductIds.containsAll(required)) {
                BigDecimal discount = computeDiscount(items, required, rule.getDiscountPercentage());

                DiscountApplication app = new DiscountApplication();
                app.setCart(cart);
                app.setBundleRule(rule);
                app.setDiscountAmount(discount);
                app.setAppliedAt(LocalDateTime.now());

                result.add(discountApplicationRepository.save(app));
            }
        }

        return result;
    }

    private Set<Long> parseCsvIds(String csv) {
        if (csv == null || csv.trim().isEmpty()) return Collections.emptySet();
        Set<Long> ids = new HashSet<>();
        for (String part : csv.split(",")) {
            String t = part.trim();
            if (!t.isEmpty()) ids.add(Long.parseLong(t));
        }
        return ids;
    }

    private BigDecimal computeDiscount(List<CartItem> items, Set<Long> requiredIds, Double pct) {
        double percent = pct == null ? 0.0 : pct;
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem ci : items) {
            if (ci.getProduct() == null || ci.getProduct().getId() == null) continue;
            if (!requiredIds.contains(ci.getProduct().getId())) continue;

            BigDecimal price = ci.getProduct().getPrice() == null ? BigDecimal.ZERO : ci.getProduct().getPrice();
            int qty = ci.getQuantity() == null ? 0 : ci.getQuantity();
            total = total.add(price.multiply(BigDecimal.valueOf(qty)));
        }

        return total.multiply(BigDecimal.valueOf(percent)).divide(BigDecimal.valueOf(100));
    }
}
