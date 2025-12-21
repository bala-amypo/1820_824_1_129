package com.example.demo.controller;

import com.example.demo.model.BundleRule;
import com.example.demo.service.BundleRuleService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/api/bundle-rules")
@Tag(name = "Bundle Rules")
public class BundleRuleController {

    private final BundleRuleService service;

    public BundleRuleController(BundleRuleService service) {
        this.service = service;
    }

    @PostMapping
    public BundleRule create(@RequestBody BundleRule rule) {
        return service.createRule(rule);
    }

    @PutMapping("/{id}")
    public BundleRule update(@PathVariable Long id, @RequestBody BundleRule rule) {
        return service.updateRule(id, rule);
    }

    @GetMapping("/{id}")
    public BundleRule get(@PathVariable Long id) {
        return service.getRuleById(id);
    }

    @GetMapping("/active")
    public List<BundleRule> activeRules() {
        return service.getActiveRules();
    }

    @PutMapping("/{id}/deactivate")
    public void deactivate(@PathVariable Long id) {
        service.deactivateRule(id);
    }
}
