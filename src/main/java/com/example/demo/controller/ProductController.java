package com.example.demo.controller;

import com.example.demo.dto.CreateProductDto;
import com.example.demo.dto.UpdateProductDto;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductDto dto) {
        Product product = new Product();
        product.setSku(dto.getSku());
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        Product saved = productService.createProduct(product);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody UpdateProductDto dto) {
        Product product = new Product();
        product.setSku(dto.getSku());
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        Product updated = productService.updateProduct(id, product);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateProduct(@PathVariable Long id) {
        productService.deactivateProduct(id);
        return ResponseEntity.ok().build();
    }
}
