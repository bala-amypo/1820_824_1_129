package com.example.demo.service.impl;

import com.example.demo.model.BundleRule;
import com.example.demo.repository.BundleRuleRepository;
import com.example.demo.service.BundleRuleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BundleRuleServiceImpl implements BundleRuleService {
private final BundleRuleRepository bundleRuleRepository;

public BundleRuleServiceImpl(BundleRuleRepository bundleRuleRepository) {
    this.bundleRuleRepository = bundleRuleRepository;
}

@Override
public BundleRule createRule(BundleRule rule) {
    if (rule.getDiscountPercentage() < 1 || rule.getDiscountPercentage() > 100) {
        throw new IllegalArgumentException("Invalid discount percentage");
    }
    if (rule.getRequiredProductIds() == null || rule.getRequiredProductIds().isBlank()) {
        throw new IllegalArgumentException("Required products cannot be empty");
    }
    rule.setActive(true);
    return bundleRuleRepository.save(rule);
}

@Override
public void deactivateRule(Long ruleId) {
    BundleRule rule = bundleRuleRepository.findById(ruleId).orElseThrow();
    rule.setActive(false);
    bundleRuleRepository.save(rule);
}

@Override
public List<BundleRule> getActiveRules() {
    return bundleRuleRepository.findAll()
            .stream()
            .filter(BundleRule::getActive)
            .collect(Collectors.toList());
}
}