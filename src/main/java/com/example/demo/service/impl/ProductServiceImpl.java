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

    private final ProductRepository repo;

    public ProductServiceImpl(ProductRepository repo) {
        this.repo = repo;
    }

    @Override
    public Product createProduct(Product product) {
        if (product.getPrice() == null ||
            product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        return repo.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product updated) {
        Product existing = getProductById(id);

        existing.setName(updated.getName());
        existing.setCategory(updated.getCategory());
        existing.setSku(updated.getSku());

        if (updated.getPrice() != null) {
            if (updated.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Price must be positive");
            }
            existing.setPrice(updated.getPrice());
        }

        return repo.save(existing);
    }

    @Override
    public Product updateProductPrice(Long id, double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }

        Product product = getProductById(id);
        product.setPrice(BigDecimal.valueOf(price));
        return repo.save(product);
    }

    @Override
    public Product getProductById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    @Override
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    @Override
    public void deactivateProduct(Long id) {
        Product product = getProductById(id);
        product.setActive(false);
        repo.save(product);
    }
}
