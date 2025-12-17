package com.example.demo.controller;

import com.example.demo.model.BundleRule;
import com.example.demo.service.BundleRuleService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RestController
@RequestMapping("/api/bundle-rules")
@Tag(name = "Bundle Rules")
public class BundleRuleController {

    private final BundleRuleService bundleRuleService;

    public BundleRuleController(BundleRuleService bundleRuleService) {
        this.bundleRuleService = bundleRuleService;
    }

    @PostMapping
    public BundleRule create(@RequestBody BundleRule rule) {
        return bundleRuleService.createRule(rule);
    }

    @PutMapping("/{id}")
    public BundleRule update(@PathVariable Long id, @RequestBody BundleRule rule) {
        return bundleRuleService.updateRule(id, rule);
    }

    @GetMapping("/{id}")
    public BundleRule get(@PathVariable Long id) {
        return bundleRuleService.getRuleById(id);
    }

    @GetMapping("/active")
    public List<BundleRule> getActive() {
        return bundleRuleService.getActiveRules();
    }

    @PutMapping("/{id}/deactivate")
    public void deactivate(@PathVariable Long id) {
        bundleRuleService.deactivateRule(id);
    }
}
