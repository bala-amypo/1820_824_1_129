package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // ======================================
    // Used by services
    // ======================================
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

    List<CartItem> findByCartId(Long cartId);

    // ======================================
    // Used by TESTS (custom query)
    // ======================================
    @Query("""
        SELECT ci FROM CartItem ci
        WHERE ci.cart.id = :cartId
          AND ci.quantity >= :minQuantity
    """)
    List<CartItem> findByCartIdAndMinQuantity(
            @Param("cartId") Long cartId,
            @Param("minQuantity") int minQuantity
    );
}
