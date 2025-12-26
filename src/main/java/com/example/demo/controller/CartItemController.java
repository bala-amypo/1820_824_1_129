package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.demo.model.CartItem;
import com.example.demo.service.CartItemService;

@RestController
@RequestMapping("/cart-items")
public class CartItemController {

    private final CartItemService service;

    public CartItemController(CartItemService service) {
        this.service = service;
    }

    @PostMapping
    public CartItem create(@RequestBody CartItem item) {
        return service.save(item);
    }

    @GetMapping
    public List<CartItem> getByCartIdAndMinQuantity(
            @RequestParam Long cartId,
            @RequestParam int minQuantity) {

        return service.getByCartIdAndMinQuantity(cartId, minQuantity);
    }
}
