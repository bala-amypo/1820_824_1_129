package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.service.CartService;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@Tag(name = "Carts")
public class CartController {

    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    @PostMapping("/{userId}")
    public Cart create(@PathVariable Long userId) {
        return service.createCart(userId);
    }

    @GetMapping("/{id}")
    public Cart get(@PathVariable Long id) {
        return service.getCartById(id);
    }

    @GetMapping("/user/{userId}")
    public Cart getByUser(@PathVariable Long userId) {
        return service.getCartByUserId(userId);
    }

    @PutMapping("/{id}/deactivate")
    public void deactivate(@PathVariable Long id) {
        service.deactivateCart(id);
    }
}
