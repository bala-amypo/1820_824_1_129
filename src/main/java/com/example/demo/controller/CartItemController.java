package com.example.demo.controller;

import com.example.demo.model.CartItem;
import com.example.demo.service.CartItemService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequestMapping("/api/cart-items")
@Tag(name = "Cart Items")
public class CartItemController {

    private final CartItemService service;

    public CartItemController(CartItemService service) {
        this.service = service;
    }

    @PostMapping
    public CartItem add(
            @RequestParam Long cartId,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        return service.addItem(cartId, productId, quantity);
    }

    @PutMapping("/{id}")
    public CartItem update(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        return service.updateItem(id, quantity);
    }

    @GetMapping("/cart/{cartId}")
    public List<CartItem> list(@PathVariable Long cartId) {
        return service.getItemsForCart(cartId);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        service.removeItem(id);
    }
}
