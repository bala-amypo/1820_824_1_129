package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/{userId}")
    public Cart create(@PathVariable Long userId) {
        return cartService.createCart(userId);
    }

    @GetMapping("/{userId}")
    public Cart get(@PathVariable Long userId) {
        return cartService.getActiveCartForUser(userId);
    }
}
