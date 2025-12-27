package com.example.demo.service.impl;

import com.example.demo.model.BundleRule;
import com.example.demo.repository.BundleRuleRepository;
import com.example.demo.service.BundleRuleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BundleRuleServiceImpl implements BundleRuleService {

    private final BundleRuleRepository repo;

    public BundleRuleServiceImpl(BundleRuleRepository repo) {
        this.repo = repo;
    }

    @Override
    public BundleRule createRule(BundleRule rule) {

        if (rule.getDiscountPercentage() <= 0 || rule.getDiscountPercentage() > 100) {
            throw new IllegalArgumentException("Invalid discount percentage");
        }

        if (rule.getRequiredProductIds() == null || rule.getRequiredProductIds().isBlank()) {
            throw new IllegalArgumentException("Required products missing");
        }

        rule.setActive(true);
        return repo.save(rule);
    }

    @Override
    public BundleRule updateRule(Long id, BundleRule updated) {

        BundleRule existing = repo.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        if (updated.getRuleName() != null) {
            existing.setRuleName(updated.getRuleName());
        }

        if (updated.getDiscountPercentage() > 0 &&
            updated.getDiscountPercentage() <= 100) {
            existing.setDiscountPercentage(updated.getDiscountPercentage());
        }

        if (updated.getRequiredProductIds() != null &&
            !updated.getRequiredProductIds().isBlank()) {
            existing.setRequiredProductIds(updated.getRequiredProductIds());
        }

        return repo.save(existing);
    }

    @Override
    public void deactivateRule(Long id) {
        BundleRule rule = repo.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        rule.setActive(false);
        repo.save(rule);
    }

    @Override
    public BundleRule getRuleById(Long id) {
        return repo.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public List<BundleRule> getActiveRules() {
        return repo.findAll()
                .stream()
                .filter(BundleRule::getActive)
                .collect(Collectors.toList());
    }
}
