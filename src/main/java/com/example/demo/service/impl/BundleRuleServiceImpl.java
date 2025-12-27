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
        if (rule.getDiscountPercentage() <= 0 || rule.getDiscountPercentage() > 100) {
            throw new IllegalArgumentException("Discount must be between 1 and 100");
        }
        if (rule.getRequiredProductIdsCsv() == null || rule.getRequiredProductIdsCsv().isBlank()) {
            throw new IllegalArgumentException("Required products cannot be empty");
        }
        return bundleRuleRepository.save(rule);
    }

    @Override
    public BundleRule updateRule(Long id, BundleRule rule) {
        BundleRule existing = getRuleById(id);

        existing.setDiscountPercentage(rule.getDiscountPercentage());
        existing.setRequiredProductIdsCsv(rule.getRequiredProductIdsCsv());
        existing.setMinQuantity(rule.getMinQuantity());

        return bundleRuleRepository.save(existing);
    }

    @Override
    public BundleRule getRuleById(Long id) {
        return bundleRuleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bundle rule not found"));
    }

    @Override
    public List<BundleRule> getAllRules() {
        return bundleRuleRepository.findAll();
    }
}
