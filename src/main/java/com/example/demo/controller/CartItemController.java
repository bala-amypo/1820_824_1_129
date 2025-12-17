package com.example.demo.controller;

import com.example.demo.model.CartItem;
import com.example.demo.service.CartItemService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
@RequestMapping("/api/cart-items")
@Tag(name = "Cart Items")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping
    public CartItem addItem(
            @RequestParam Long cartId,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        return cartItemService.addItem(cartId, productId, quantity);
    }

    @PutMapping("/{id}")
    public CartItem updateItem(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        return cartItemService.updateItem(id, quantity);
    }

    @GetMapping("/cart/{cartId}")
    public List<CartItem> listItems(@PathVariable Long cartId) {
        return cartItemService.getItemsForCart(cartId);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        cartItemService.removeItem(id);
    }
}
