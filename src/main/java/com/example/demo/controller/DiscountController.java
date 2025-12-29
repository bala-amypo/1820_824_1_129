package com.example.demo.controller;

import com.example.demo.model.DiscountApplication;
import com.example.demo.service.DiscountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discounts")
@Tag(name = "Discounts", description = "Discount evaluation APIs")
@PreAuthorize("hasRole('CUSTOMER')")
public class DiscountController {

    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @Operation(summary = "Evaluate bundle discounts for cart")
    @PostMapping("/evaluate/{cartId}")
    public List<DiscountApplication> evaluate(@PathVariable Long cartId) {
        return discountService.evaluateDiscounts(cartId);
    }

    @Operation(summary = "Get discount application by ID")
    @GetMapping("/{id}")
    public DiscountApplication getById(@PathVariable Long id) {
        return discountService.getApplicationById(id);
    }

    @Operation(summary = "Get discounts applied to cart")
    @GetMapping("/cart/{cartId}")
    public List<DiscountApplication> getForCart(@PathVariable Long cartId) {
        return discountService.getApplicationsForCart(cartId);
    }
}
