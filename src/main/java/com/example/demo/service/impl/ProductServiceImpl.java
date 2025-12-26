package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
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
    if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
        throw new IllegalArgumentException("Price must be positive");
    }
    return productRepository.save(product);
}

@Override
public Product updateProduct(Long id, Product updated) {
    Product existing = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

    if (updated.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
        throw new IllegalArgumentException("Price must be positive");
    }

    existing.setName(updated.getName());
    existing.setSku(updated.getSku());
    existing.setPrice(updated.getPrice());
    existing.setActive(updated.getActive());

    return productRepository.save(existing);
}

@Override
public Product getProductById(Long id) {
    return productRepository.findById(id).orElse(null);
}

@Override
public List<Product> getAllProducts() {
    return productRepository.findAll();
}

@Override
public void deactivateProduct(Long id) {
    Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    product.setActive(false);
    productRepository.save(product);
}
}