package com.example.demo.service.impl;

import com.example.demo.model.BundleRule;
import com.example.demo.repository.BundleRuleRepository;
import com.example.demo.service.BundleRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BundleRuleServiceImpl implements BundleRuleService {

    @Autowired
    private BundleRuleRepository bundleRuleRepository;

    @Override
    public BundleRule createRule(BundleRule rule) {
        if (rule.getRequiredProductIds() == null || rule.getRequiredProductIds().trim().isEmpty()) {
            throw new IllegalArgumentException("Required product IDs cannot be empty");
        }
        
        if (rule.getDiscountPercentage() == null || 
            rule.getDiscountPercentage() < 0 || rule.getDiscountPercentage() > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }
        
        rule.setActive(true);
        return bundleRuleRepository.save(rule);
    }
}
