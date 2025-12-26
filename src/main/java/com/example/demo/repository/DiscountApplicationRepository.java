package com.example.demo.repository;

import com.example.demo.model.DiscountApplication;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountApplicationRepository
        extends JpaRepository<DiscountApplication, Long> {

    void deleteByCartId(Long cartId);

    List<DiscountApplication> findByCartId(Long cartId);
}
