package com.example.demo.service.impl;

import com.example.demo.model.BundleRule;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.DiscountApplication;
import com.example.demo.repository.BundleRuleRepository;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.DiscountApplicationRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class DiscountServiceImpl {

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

    public List<DiscountApplication> evaluateDiscounts(Long cartId) {

        Cart cart = cartRepository.findById(cartId).orElseThrow();

        if (!cart.getActive()) {
            return Collections.emptyList();
        }

        List<CartItem> items = cartItemRepository.findByCartId(cartId);
        Map<Long, CartItem> itemMap = new HashMap<>();

        for (CartItem ci : items) {
            itemMap.put(ci.getProduct().getId(), ci);
        }

        discountApplicationRepository.deleteByCartId(cartId);

        List<DiscountApplication> result = new ArrayList<>();

        for (BundleRule rule : bundleRuleRepository.findByActiveTrue()) {

            String[] ids = rule.getRequiredProductIds().split(",");
            boolean valid = true;
            BigDecimal total = BigDecimal.ZERO;

            for (String id : ids) {
                Long pid = Long.parseLong(id.trim());
                if (!itemMap.containsKey(pid)) {
                    valid = false;
                    break;
                }
                CartItem ci = itemMap.get(pid);
                total = total.add(
                        ci.getProduct().getPrice()
                          .multiply(BigDecimal.valueOf(ci.getQuantity()))
                );
            }

            if (valid) {
                DiscountApplication app = new DiscountApplication();
                app.setCart(cart);
                app.setBundleRule(rule);
                app.setDiscountAmount(
                        total.multiply(
                                BigDecimal.valueOf(rule.getDiscountPercentage() / 100)
                        )
                );
                app.setAppliedAt(LocalDateTime.now());
                result.add(discountApplicationRepository.save(app));
            }
        }

        return result;
    }
}
