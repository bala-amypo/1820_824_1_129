package com.example.demo.service.impl;

import com.example.demo.model.BundleRule;
import com.example.demo.repository.BundleRuleRepository;
import com.example.demo.service.BundleRuleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BundleRuleServiceImpl implements BundleRuleService {

    private final BundleRuleRepository bundleRuleRepository;

    public BundleRuleServiceImpl(BundleRuleRepository bundleRuleRepository) {
        this.bundleRuleRepository = bundleRuleRepository;
    }

    @Override
    public BundleRule createRule(BundleRule rule) {
        if (!rule.isDiscountPercentageValid()) {
            throw new IllegalArgumentException("Invalid discount percentage");
        }

        if (rule.getRequiredProductIds() == null || rule.getRequiredProductIds().isBlank()) {
            throw new IllegalArgumentException("Required products cannot be empty");
        }

        return bundleRuleRepository.save(rule);
    }

    @Override
    public BundleRule updateRule(Long id, BundleRule rule) {
        BundleRule existing = getRuleById(id);

        if (!rule.isDiscountPercentageValid()) {
            throw new IllegalArgumentException("Invalid discount percentage");
        }

        existing.setRuleName(rule.getRuleName());
        existing.setDiscountPercentage(rule.getDiscountPercentage());
        existing.setRequiredProductIds(rule.getRequiredProductIds());

        return bundleRuleRepository.save(existing);
    }

    @Override
    public void deactivateRule(Long id) {
        BundleRule rule = getRuleById(id);
        rule.setActive(false);
        bundleRuleRepository.save(rule);
    }

    @Override
    public BundleRule getRuleById(Long id) {
        return bundleRuleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rule not found"));
    }

    @Override
    public List<BundleRule> getAllRules() {
        return bundleRuleRepository.findAll();
    }

    @Override
    public List<BundleRule> getActiveRules() {
        return bundleRuleRepository.findAll().stream()
                .filter(BundleRule::getActive)
                .toList();
    }
}
