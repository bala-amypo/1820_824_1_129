package com.example.demo.service;

import java.util.Optional;

import com.example.demo.model.Product;

public interface ProductService {

    Product save(Product product);

    Optional<Product> getById(Long id);

    void deactivateProduct(Long id);
}
