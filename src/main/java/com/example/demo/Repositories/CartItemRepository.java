package com.example.demo.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.CartItem;
import com.example.demo.Models.UserPost;
import com.example.demo.Models.Cart;


public interface CartItemRepository extends JpaRepository<CartItem, Long>{
    Optional<CartItem> findByCartAndProduct(Cart cart, UserPost product);
}
