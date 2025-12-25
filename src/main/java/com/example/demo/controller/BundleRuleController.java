package com.example.demo.controller;

import com.example.demo.dto.CreateBundleRuleDto;
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
    public ResponseEntity<BundleRule> createRule(@RequestBody CreateBundleRuleDto dto) {
        BundleRule rule = new BundleRule();
        rule.setRuleName(dto.getRuleName());
        rule.setRequiredProductIds(dto.getRequiredProductIds());
        rule.setDiscountPercentage(dto.getDiscountPercentage());
        
        BundleRule saved = bundleRuleService.createRule(rule);
        return ResponseEntity.ok(saved);
    }
}
