package com.example.demo.service.impl;

import com.example.demo.model.BundleRule;
import com.example.demo.repository.BundleRuleRepository;
import com.example.demo.service.BundleRuleService;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BundleRuleServiceImpl implements BundleRuleService {

    private final BundleRuleRepository bundleRuleRepository;

    public BundleRuleServiceImpl(BundleRuleRepository bundleRuleRepository) {
        this.bundleRuleRepository = bundleRuleRepository;
    }

    @Override
    public BundleRule createRule(BundleRule rule) {

        if (rule.getDiscountPercentage() == null ||
                rule.getDiscountPercentage() < 1 ||
                rule.getDiscountPercentage() > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 1 and 100");
        }

        if (rule.getRequiredProductIds() == null ||
                rule.getRequiredProductIds().isBlank()) {
            throw new IllegalArgumentException("Required product IDs cannot be empty");
        }

        String cleaned = rule.getRequiredProductIds().trim();
        if (cleaned.replace(",", "").isBlank()) {
            throw new IllegalArgumentException("Invalid product ID list");
        }

        rule.setRequiredProductIds(cleaned);
        rule.setActive(true);

        return bundleRuleRepository.save(rule);
    }

    @Override
    public BundleRule updateRule(Long id, BundleRule updatedRule) {

        BundleRule existing = bundleRuleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bundle rule not found"));

        if (updatedRule.getDiscountPercentage() != null) {
            if (updatedRule.getDiscountPercentage() < 1 ||
                    updatedRule.getDiscountPercentage() > 100) {
                throw new IllegalArgumentException("Discount percentage must be between 1 and 100");
            }
            existing.setDiscountPercentage(updatedRule.getDiscountPercentage());
        }

        if (updatedRule.getRequiredProductIds() != null &&
                !updatedRule.getRequiredProductIds().isBlank()) {

            String cleaned = updatedRule.getRequiredProductIds().trim();
            if (cleaned.replace(",", "").isBlank()) {
                throw new IllegalArgumentException("Invalid product ID list");
            }
            existing.setRequiredProductIds(cleaned);
        }

        return bundleRuleRepository.save(existing);
    }

    @Override
    public void deactivateRule(Long ruleId) {
        BundleRule rule = bundleRuleRepository.findById(ruleId)
                .orElseThrow(() -> new IllegalArgumentException("Bundle rule not found"));

        if (Boolean.FALSE.equals(rule.getActive())) {
            return;
        }

        rule.setActive(false);
        bundleRuleRepository.save(rule);
    }

    @Override
    public List<BundleRule> getActiveRules() {
        return bundleRuleRepository.findAll()
                .stream()
                .filter(r -> Boolean.TRUE.equals(r.getActive()))
                .collect(Collectors.toList());
    }

    @Override
    public BundleRule getRuleById(Long id) {
        return bundleRuleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bundle rule not found"));
    }
}
