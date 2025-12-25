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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DiscountServiceImpl implements DiscountService {

    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Autowired
    private BundleRuleRepository bundleRuleRepository;
    
    @Autowired
    private DiscountApplicationRepository discountApplicationRepository;

    @Override
    public List<DiscountApplication> evaluateDiscounts(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new EntityNotFoundException("Cart not found"));
        
        if (!cart.getActive()) {
            return new ArrayList<>();
        }
        
        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);
        List<BundleRule> activeRules = bundleRuleRepository.findByActiveTrue();
        List<DiscountApplication> applications = new ArrayList<>();
        
        // Clear existing discount applications
        discountApplicationRepository.deleteByCartId(cartId);
        
        for (BundleRule rule : activeRules) {
            try {
                String[] requiredProductIds = rule.getRequiredProductIds().split(",");
                boolean allProductsPresent = true;
                
                for (String productIdStr : requiredProductIds) {
                    Long productId = Long.parseLong(productIdStr.trim());
                    boolean productInCart = cartItems.stream()
                        .anyMatch(item -> item.getProduct().getId().equals(productId));
                    if (!productInCart) {
                        allProductsPresent = false;
                        break;
                    }
                }
                
                if (allProductsPresent && !cartItems.isEmpty()) {
                    BigDecimal subtotal = cartItems.stream()
                        .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
                    BigDecimal discountAmount = subtotal.multiply(BigDecimal.valueOf(rule.getDiscountPercentage() / 100));
                    
                    DiscountApplication application = new DiscountApplication();
                    application.setCart(cart);
                    application.setBundleRule(rule);
                    application.setDiscountAmount(discountAmount);
                    application.setAppliedAt(LocalDateTime.now());
                    
                    DiscountApplication saved = discountApplicationRepository.save(application);
                    applications.add(saved);
                }
            } catch (Exception e) {
                // Continue with next rule if parsing fails
                continue;
            }
        }
        
        return applications;
    }
}
