package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.service.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@Tag(name = "Carts", description = "Shopping cart APIs")
@PreAuthorize("hasRole('CUSTOMER')")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(summary = "Create cart for user")
    @PostMapping("/{userId}")
    public Cart createCart(@PathVariable Long userId) {
        return cartService.createCart(userId);
    }

    @Operation(summary = "Get cart by ID")
    @GetMapping("/{id}")
    public Cart getCartById(@PathVariable Long id) {
        return cartService.getCartById(id);
    }

    @Operation(summary = "Get cart by user ID")
    @GetMapping("/user/{userId}")
    public Cart getCartByUser(@PathVariable Long userId) {
        return cartService.getCartByUserId(userId);
    }

    @Operation(summary = "Deactivate cart")
    @PutMapping("/{id}/deactivate")
    public void deactivateCart(@PathVariable Long id) {
        cartService.deactivateCart(id);
    }
}
