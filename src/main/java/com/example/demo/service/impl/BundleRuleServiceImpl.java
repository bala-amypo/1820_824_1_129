package com.example.demo.service.impl;

import com.example.demo.model.BundleRule;
import com.example.demo.repository.BundleRuleRepository;
import com.example.demo.service.BundleRuleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BundleRuleServiceImpl implements BundleRuleService {

    private final BundleRuleRepository repo;

    public BundleRuleServiceImpl(BundleRuleRepository repo) {
        this.repo = repo;
    }

    @Override
    public BundleRule createRule(BundleRule rule) {
        if (!rule.isDiscountPercentageValid()) {
            throw new IllegalArgumentException("Invalid discount percentage");
        }
        if (rule.getRequiredProductIds() == null ||
            rule.getRequiredProductIds().isBlank()) {
            throw new IllegalArgumentException("Required products cannot be empty");
        }
        rule.setActive(true);
        return repo.save(rule);
    }

    @Override
    public BundleRule updateRule(Long id, BundleRule rule) {
        BundleRule existing = getRuleById(id);

        existing.setRuleName(rule.getRuleName());
        existing.setDiscountPercentage(rule.getDiscountPercentage());
        existing.setRequiredProductIds(rule.getRequiredProductIds());

        return repo.save(existing);
    }

    @Override
    public BundleRule getRuleById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bundle rule not found"));
    }

    @Override
    public List<BundleRule> getAllRules() {
        return repo.findAll();
    }

    @Override
    public void deactivateRule(Long id) {
        BundleRule rule = getRuleById(id);
        rule.setActive(false);
        repo.save(rule);
    }
}
