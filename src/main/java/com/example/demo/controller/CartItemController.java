package com.example.demo.controller;

import com.example.demo.dto.CartItemDto;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.service.CartItemService;
import com.example.demo.service.CartService;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<CartItem> addItemToCart(@RequestBody CartItemDto dto) {
        Cart cart = cartService.getActiveCartForUser(1L); // Simplified for demo
        Product product = productService.getProductById(dto.getProductId());
        
        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(dto.getQuantity());
        
        CartItem saved = cartItemService.addItemToCart(item);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/cart/{cartId}")
    public ResponseEntity<List<CartItem>> getItemsForCart(@PathVariable Long cartId) {
        List<CartItem> items = cartItemService.getItemsForCart(cartId);
        return ResponseEntity.ok(items);
    }
}
