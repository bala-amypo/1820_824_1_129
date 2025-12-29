package com.example.demo.controller;

import com.example.demo.model.BundleRule;
import com.example.demo.service.BundleRuleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bundle-rules")
@Tag(name = "Bundle Rules", description = "Bundle & Save rule APIs")
@PreAuthorize("hasRole('MERCHANT')")
public class BundleRuleController {

    private final BundleRuleService bundleRuleService;

    public BundleRuleController(BundleRuleService bundleRuleService) {
        this.bundleRuleService = bundleRuleService;
    }

    @Operation(summary = "Create bundle rule")
    @PostMapping
    public BundleRule createRule(@RequestBody BundleRule rule) {
        return bundleRuleService.createRule(rule);
    }

    @Operation(summary = "Update bundle rule")
    @PutMapping("/{id}")
    public BundleRule updateRule(
            @PathVariable Long id,
            @RequestBody BundleRule rule) {

        return bundleRuleService.updateRule(id, rule);
    }

    @Operation(summary = "Get bundle rule by ID")
    @GetMapping("/{id}")
    public BundleRule getRule(@PathVariable Long id) {
        return bundleRuleService.getRuleById(id);
    }

    @Operation(summary = "Get active bundle rules")
    @GetMapping("/active")
    public List<BundleRule> getActiveRules() {
        return bundleRuleService.getActiveRules();
    }

    @Operation(summary = "Deactivate bundle rule")
    @PutMapping("/{id}/deactivate")
    public void deactivateRule(@PathVariable Long id) {
        bundleRuleService.deactivateRule(id);
    }
}
