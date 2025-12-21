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

import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final DiscountApplicationRepository discountRepo;
    private final BundleRuleRepository bundleRepo;
    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;

    public DiscountServiceImpl(
            DiscountApplicationRepository discountRepo,
            BundleRuleRepository bundleRepo,
            CartRepository cartRepo,
            CartItemRepository cartItemRepo) {
        this.discountRepo = discountRepo;
        this.bundleRepo = bundleRepo;
        this.cartRepo = cartRepo;
        this.cartItemRepo = cartItemRepo;
    }

    @Override
    public List<DiscountApplication> evaluateDiscounts(Long cartId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        discountRepo.deleteByCartId(cartId);

        List<CartItem> items = cartItemRepo.findByCartId(cartId);
        List<BundleRule> rules = bundleRepo.findByActiveTrue();

        for (BundleRule rule : rules) {
            BigDecimal total = BigDecimal.ZERO;

            for (CartItem item : items) {
                total = total.add(
                        item.getProduct().getPrice()
                                .multiply(BigDecimal.valueOf(item.getQuantity()))
                );
            }

            if (total.compareTo(BigDecimal.ZERO) > 0) {
                DiscountApplication app = new DiscountApplication();
                app.setCart(cart);
                app.setBundleRule(rule);
                app.setDiscountAmount(
                        total.multiply(
                                BigDecimal.valueOf(rule.getDiscountPercentage() / 100)
                        )
                );
                app.setAppliedAt(LocalDateTime.now());
                discountRepo.save(app);
            }
        }

        return discountRepo.findByCartId(cartId);
    }

    @Override
    public DiscountApplication getApplicationById(Long id) {
        return discountRepo.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("DiscountApplication not found"));
    }

    @Override
    public List<DiscountApplication> getApplicationsForCart(Long cartId) {
        return discountRepo.findByCartId(cartId);
    }
}
