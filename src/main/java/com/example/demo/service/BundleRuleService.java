package com.example.demo.service;

import com.example.demo.model.BundleRule;
import java.util.List;

public interface BundleRuleService {

    BundleRule createRule(BundleRule rule);

    BundleRule updateRule(Long id, BundleRule rule);

    BundleRule getRuleById(Long id);

    List<BundleRule> getActiveRules();

    void deactivateRule(Long id);
}
