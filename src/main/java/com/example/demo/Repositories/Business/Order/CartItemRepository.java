package com.example.demo.Repositories.Business.Order;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Models.Ecommerce.Cart.Cart;
import com.example.demo.Models.Ecommerce.Cart.CartItem;
import com.example.demo.Models.SocialMedia.UserPost;


public interface CartItemRepository extends JpaRepository<CartItem, Long>{
    Optional<CartItem> findByCartAndProduct(Cart cart, UserPost product);

    @Query(value = "SELECT * FROM cart_items WHERE product_id = :productId AND cart_id = :cartId", nativeQuery = true)
    Optional<CartItem> findCartItemByProductIdAndCartId(@Param("productId") Long productId, @Param("cartId") Long cartId);
}
