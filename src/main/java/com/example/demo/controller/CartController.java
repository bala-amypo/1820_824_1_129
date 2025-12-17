package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.service.CartService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/carts")
@Tag(name = "Carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/{userId}")
    public Cart create(@PathVariable Long userId) {
        return cartService.createCart(userId);
    }

    @GetMapping("/{id}")
    public Cart get(@PathVariable Long id) {
        return cartService.getCartById(id);
    }

    @GetMapping("/user/{userId}")
    public Cart getByUser(@PathVariable Long userId) {
        return cartService.getCartByUserId(userId);
    }

    @PutMapping("/{id}/deactivate")
    public void deactivate(@PathVariable Long id) {
        cartService.deactivateCart(id);
    }
}
