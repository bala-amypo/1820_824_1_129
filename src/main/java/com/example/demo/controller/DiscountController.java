package com.example.demo.controller;

import com.example.demo.model.DiscountApplication;
import com.example.demo.service.DiscountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/discounts")
@Tag(name = "Discounts")
public class DiscountController {

    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @PostMapping("/evaluate/{cartId}")
    public List<DiscountApplication> evaluate(@PathVariable Long cartId) {
        return discountService.evaluateDiscounts(cartId);
    }

    @GetMapping("/{id}")
    public DiscountApplication get(@PathVariable Long id) {
        return discountService.getApplicationById(id);
    }

    @GetMapping("/cart/{cartId}")
    public List<DiscountApplication> getByCart(@PathVariable Long cartId) {
        return discountService.getApplicationsForCart(cartId);
    }
}
