package com.example.demo.service.impl;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product createProduct(Product product) {

        if (productRepository.findBySku(product.getSku()).isPresent()) {
            throw new IllegalArgumentException("Duplicate SKU");
        }

        if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }

        product.setActive(true);
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product updated) {

        Product existing = productRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        if (updated.getName() != null) {
            existing.setName(updated.getName());
        }

        if (updated.getPrice() != null) {
            if (updated.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Price must be positive");
            }
            existing.setPrice(updated.getPrice());
        }

        return productRepository.save(existing);
    }

    @Override
    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void deactivateProduct(Long id) {
        Product p = getProduct(id);
        p.setActive(false);
        productRepository.save(p);
    }
}
