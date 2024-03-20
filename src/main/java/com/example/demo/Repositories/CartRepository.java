package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.Cart;

public interface CartRepository extends JpaRepository<Cart, Long>{
    
}
