package com.example.demo.repository;

import com.example.demo.model.CartItem;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCartId(Long cartId);

    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

    @Query("""
           select ci
           from CartItem ci
           where ci.cart.id = :cartId
             and ci.quantity >= :minQty
           """)
    List<CartItem> findByCartIdAndMinQuantity(
            @Param("cartId") Long cartId,
            @Param("minQty") int minQty
    );
}
