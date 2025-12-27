package com.example.demo.service.impl;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
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

        if (repo.findBySku(product.getSku()).isPresent()) {
            throw new IllegalArgumentException("Duplicate SKU");
        }

        if (product.getPrice() == null ||
            product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid price");
        }

        product.setActive(true);
        return repo.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product updated) {

        Product existing = getProductById(id);

        if (updated.getName() != null) {
            existing.setName(updated.getName());
        }

        if (updated.getPrice() != null) {
            if (updated.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Invalid price");
            }
            existing.setPrice(updated.getPrice());
        }

        return repo.save(existing);
    }

    @Override
    public Product getProductById(Long id) {
        return repo.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    @Override
    public void deactivateProduct(Long id) {
        Product p = getProductById(id);
        p.setActive(false);
        repo.save(p);
    }
}
