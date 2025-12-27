package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.DiscountService;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DiscountServiceImpl implements DiscountService {
    private final DiscountApplicationRepository discountApplicationRepository;
    private final BundleRuleRepository bundleRuleRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    
    public DiscountServiceImpl(DiscountApplicationRepository discountApplicationRepository,
                              BundleRuleRepository bundleRuleRepository,
                              CartRepository cartRepository,
                              CartItemRepository cartItemRepository) {
        this.discountApplicationRepository = discountApplicationRepository;
        this.bundleRuleRepository = bundleRuleRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }
    
    @Override
    public List<DiscountApplication> evaluateDiscounts(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null || !cart.getActive()) {
            return Collections.emptyList();
        }
        
        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);
        List<BundleRule> activeRules = bundleRuleRepository.findByActiveTrue();
        
        discountApplicationRepository.deleteByCartId(cartId);
        
        List<DiscountApplication> applications = new ArrayList<>();
        Set<Long> cartProductIds = cartItems.stream()
            .map(item -> item.getProduct().getId())
            .collect(Collectors.toSet());
        
        for (BundleRule rule : activeRules) {
            List<Long> requiredIds = Arrays.stream(rule.getRequiredProductIds().split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .collect(Collectors.toList());
            
            if (cartProductIds.containsAll(requiredIds)) {
                BigDecimal totalPrice = cartItems.stream()
                    .filter(item -> requiredIds.contains(item.getProduct().getId()))
                    .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                
                BigDecimal discountAmount = totalPrice.multiply(BigDecimal.valueOf(rule.getDiscountPercentage() / 100));
                
                if (discountAmount.compareTo(BigDecimal.ZERO) > 0) {
                    DiscountApplication application = new DiscountApplication(cart, rule, discountAmount);
                    applications.add(discountApplicationRepository.save(application));
                }
            }
        }
        
        return applications;
    }
}