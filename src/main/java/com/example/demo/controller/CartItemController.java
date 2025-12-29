package com.example.demo.controller;

import com.example.demo.model.CartItem;
import com.example.demo.service.CartItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart-items")
@Tag(name = "Cart Items", description = "Cart item APIs")
@PreAuthorize("hasRole('CUSTOMER')")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @Operation(summary = "Add item to cart")
    @PostMapping
    public CartItem addItem(
            @RequestParam Long cartId,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {

        return cartItemService.addItem(cartId, productId, quantity);
    }

    @Operation(summary = "Update cart item quantity")
    @PutMapping("/{id}")
    public CartItem updateItem(
            @PathVariable Long id,
            @RequestParam Integer quantity) {

        return cartItemService.updateItem(id, quantity);
    }

    @Operation(summary = "List items in cart")
    @GetMapping("/cart/{cartId}")
    public List<CartItem> getItems(@PathVariable Long cartId) {
        return cartItemService.getItemsForCart(cartId);
    }

    @Operation(summary = "Remove item from cart")
    @DeleteMapping("/{id}")
    public void removeItem(@PathVariable Long id) {
        cartItemService.removeItem(id);
    }
}
