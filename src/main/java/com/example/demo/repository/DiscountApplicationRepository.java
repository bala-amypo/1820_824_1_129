package com.example.demo.repository;

import com.example.demo.model.DiscountApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface DiscountApplicationRepository extends JpaRepository<DiscountApplication, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM DiscountApplication da WHERE da.cart.id = :cartId")
    void deleteByCartId(@Param("cartId") Long cartId);
    
    @Query("SELECT da FROM DiscountApplication da WHERE da.cart.id = :cartId")
    List<DiscountApplication> findByCartId(@Param("cartId") Long cartId);
}