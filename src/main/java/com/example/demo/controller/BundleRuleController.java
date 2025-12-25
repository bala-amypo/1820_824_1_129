package com.example.demo.controller;

import com.example.demo.model.BundleRule;
import com.example.demo.service.BundleRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bundle-rules")
public class BundleRuleController {

    @Autowired
    private BundleRuleService bundleRuleService;

    @PostMapping
    public ResponseEntity<BundleRule> createRule(@RequestBody BundleRule rule) {
        BundleRule saved = bundleRuleService.createRule(rule);
        return ResponseEntity.ok(saved);
    }
}
