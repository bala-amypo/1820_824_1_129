package com.example.demo.service.impl;

import com.example.demo.model.BundleRule;
import com.example.demo.repository.BundleRuleRepository;
import com.example.demo.service.BundleRuleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BundleRuleServiceImpl implements BundleRuleService {

    private final BundleRuleRepository repository;

    public BundleRuleServiceImpl(BundleRuleRepository repository) {
        this.repository = repository;
    }

    @Override
    public BundleRule createRule(BundleRule rule) {

        if (!isValidDiscountRange(rule)) {
            throw new IllegalArgumentException("Invalid discount");
        }

        if (rule.getRequiredProductIds() == null || rule.getRequiredProductIds().isBlank()) {
            throw new IllegalArgumentException("Required products missing");
        }

        rule.setActive(true);
        return repository.save(rule);
    }

    // ✅ REQUIRED BY TEST
    public boolean isValidDiscountRange(BundleRule rule) {
        return rule.getDiscountPercentage() >= 1
                && rule.getDiscountPercentage() <= 100;
    }

    @Override
    public List<BundleRule> getActiveRules() {
        return repository.findAll()
                .stream()
                .filter(BundleRule::getActive)
                .collect(Collectors.toList());
    }
}
