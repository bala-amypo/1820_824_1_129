package com.example.demo.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product save(Product product) {

        if (product.getPrice() <= 0) {
            throw new IllegalArgumentException("Invalid price");
        }

        product.setActive(true);
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> getById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public void deactivateProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow();
        product.setActive(false);
        productRepository.save(product);
    }
}
