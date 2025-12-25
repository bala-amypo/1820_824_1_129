package com.example.demo.repository;

import com.example.demo.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // cart.id
    List<CartItem> findByCart_Id(Long cartId);

    // cart.id AND product.id
    Optional<CartItem> findByCart_IdAndProduct_Id(Long cartId, Long productId);

    // cart.id AND quantity >= ?
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.quantity >= :minQuantity")
    List<CartItem> findByCartIdAndMinQuantity(@Param("cartId") Long cartId,
                                              @Param("minQuantity") Integer minQuantity);
}
