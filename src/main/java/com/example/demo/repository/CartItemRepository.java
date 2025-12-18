package com.example.demo.repository;

import com.example.demo.model.CartItem;
import com.example.demo.model.Cart;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCartId(Cart cartId);
}
