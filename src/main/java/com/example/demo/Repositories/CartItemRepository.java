package com.example.demo.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.Ecommerce.Cart.Cart;
import com.example.demo.Models.Ecommerce.Cart.CartItem;
import com.example.demo.Models.SocialMedia.UserPost;


public interface CartItemRepository extends JpaRepository<CartItem, Long>{
    Optional<CartItem> findByCartAndProduct(Cart cart, UserPost product);
}
