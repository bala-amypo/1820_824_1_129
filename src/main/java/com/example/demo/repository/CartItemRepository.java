package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // ===============================
    // Used by CartItemServiceImpl
    // ===============================
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

    // ===============================
    // Used by controllers & services
    // ===============================
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

    List<CartItem> findByCartId(Long cartId);

    // ===============================
    // Used by TESTS (quantity filter)
    // ===============================
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
