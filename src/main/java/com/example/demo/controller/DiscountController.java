package com.example.demo.controller;

import com.example.demo.model.DiscountApplication;
import com.example.demo.service.DiscountService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/api/discounts")
@Tag(name = "Discounts")
public class DiscountController {

    private final DiscountService service;

    public DiscountController(DiscountService service) {
        this.service = service;
    }

    @PostMapping("/evaluate/{cartId}")
    public List<DiscountApplication> evaluate(@PathVariable Long cartId) {
        return service.evaluateDiscounts(cartId);
    }

    @GetMapping("/{id}")
    public DiscountApplication get(@PathVariable Long id) {
        return service.getApplicationById(id);
    }

    @GetMapping("/cart/{cartId}")
    public List<DiscountApplication> getForCart(@PathVariable Long cartId) {
        return service.getApplicationsForCart(cartId);
    }
}
