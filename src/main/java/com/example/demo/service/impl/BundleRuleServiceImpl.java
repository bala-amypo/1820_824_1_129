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

        if (rule.getDiscountPercentage() <= 0 ||
            rule.getDiscountPercentage() > 100) {
            throw new IllegalArgumentException("Invalid discount");
        }

        if (rule.getRequiredProductIds() == null ||
            rule.getRequiredProductIds().isBlank()) {
            throw new IllegalArgumentException("Required products missing");
        }

        rule.setActive(true);
        return repo.save(rule);
    }

    @Override
    public void deactivateRule(Long id) {
        BundleRule rule = repo.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        rule.setActive(false);
        repo.save(rule);
    }

    @Override
    public List<BundleRule> getActiveRules() {
        return repo.findAll()
                .stream()
                .filter(BundleRule::getActive)
                .collect(Collectors.toList());
    }
}
