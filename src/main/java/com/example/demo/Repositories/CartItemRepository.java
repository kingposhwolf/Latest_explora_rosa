package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{
    
}
