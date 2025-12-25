package com.example.demo.controller;

import com.example.demo.model.CartItem;
import com.example.demo.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @PostMapping
    public ResponseEntity<CartItem> addItemToCart(@RequestBody CartItem item) {
        CartItem saved = cartItemService.addItemToCart(item);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/cart/{cartId}")
    public ResponseEntity<List<CartItem>> getItemsForCart(@PathVariable Long cartId) {
        List<CartItem> items = cartItemService.getItemsForCart(cartId);
        return ResponseEntity.ok(items);
    }
}
