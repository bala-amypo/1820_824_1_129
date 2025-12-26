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

        if (product.getPrice() == null ||
                product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }

        // Duplicate SKU check WITHOUT existsBySku
        productRepository.findAll().forEach(p -> {
            if (p.getSku().equals(product.getSku())) {
                throw new IllegalArgumentException("Duplicate SKU");
            }
        });

        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product updatedProduct) {

        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        if (updatedProduct.getPrice() != null) {
            if (updatedProduct.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Price must be positive");
            }
            existing.setPrice(updatedProduct.getPrice());
        }

        if (updatedProduct.getName() != null) {
            existing.setName(updatedProduct.getName());
        }

        return productRepository.save(existing);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    @Override
    public void deactivateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        product.setActive(false);
        productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
