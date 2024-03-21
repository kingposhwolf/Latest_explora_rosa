package com.example.demo.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.Cart;
import com.example.demo.Models.Profile;


public interface CartRepository extends JpaRepository<Cart, Long>{
    Optional<Cart> findByCustomer(Profile customer);
}
