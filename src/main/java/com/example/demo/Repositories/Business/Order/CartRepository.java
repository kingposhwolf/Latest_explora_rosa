package com.example.demo.Repositories.Business.Order;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.Ecommerce.Cart.Cart;
import com.example.demo.Models.UserManagement.Profile;



public interface CartRepository extends JpaRepository<Cart, Long>{
    Optional<Cart> findByCustomer(Profile customer);
}
