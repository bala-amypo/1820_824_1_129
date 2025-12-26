package com.example.demo.service.impl;

import com.example.demo.model.BundleRule;
import com.example.demo.repository.BundleRuleRepository;

public class BundleRuleServiceImpl {

    private final BundleRuleRepository bundleRuleRepository;

    public BundleRuleServiceImpl(BundleRuleRepository bundleRuleRepository) {
        this.bundleRuleRepository = bundleRuleRepository;
    }

    public BundleRule createRule(BundleRule rule) {
        if (rule.getDiscountPercentage() < 0
                || rule.getDiscountPercentage() > 100) {
            throw new IllegalArgumentException("Invalid discount");
        }
        return bundleRuleRepository.save(rule);
    }
}
