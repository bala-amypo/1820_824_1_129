package com.example.demo.service.impl;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product createProduct(Product product) {
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }

        if (productRepository.findBySku(product.getSku()).isPresent()) {
            throw new IllegalArgumentException("Duplicate SKU");
        }

        return productRepository.save(product);
    }

    // ✅ REQUIRED BY INTERFACE
    @Override
    public Product updateProduct(Long id, Product updated) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        if (updated.getPrice() != null &&
            updated.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }

        existing.setName(updated.getName());
        existing.setCategory(updated.getCategory());
        existing.setPrice(updated.getPrice());
        existing.setActive(updated.getActive());

        return productRepository.save(existing);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public void deactivateProduct(Long id) {
        Product product = getProductById(id);
        product.setActive(false);
        productRepository.save(product);
    }
}
