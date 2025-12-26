package com.example.demo.service.impl;

import com.example.demo.model.BundleRule;
import com.example.demo.repository.BundleRuleRepository;
import com.example.demo.service.BundleRuleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BundleRuleServiceImpl implements BundleRuleService {

    private final BundleRuleRepository bundleRuleRepository;

    public BundleRuleServiceImpl(BundleRuleRepository bundleRuleRepository) {
        this.bundleRuleRepository = bundleRuleRepository;
    }

    @Override
    public BundleRule create(BundleRule rule) {

        if (rule.getDiscountPercentage() < 0 || rule.getDiscountPercentage() > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100");
        }

        if (rule.getRequiredProductIds() == null || rule.getRequiredProductIds().isBlank()) {
            throw new IllegalArgumentException("Required products cannot be empty");
        }

        return bundleRuleRepository.save(rule);
    }

    @Override
    public List<BundleRule> getAll() {
        return bundleRuleRepository.findAll();
    }
}
