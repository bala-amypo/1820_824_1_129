package com.example.demo.repository;

import com.example.demo.model.DiscountApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscountApplicationRepository
        extends JpaRepository<DiscountApplication, Long> {

    // Fetch all discount records for a cart
    List<DiscountApplication> findByCartId(Long cartId);
}
