package com.example.demo.Repositories.Business.Order;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Models.Ecommerce.Cart.Cart;
import com.example.demo.Models.Ecommerce.Cart.CartItem;
import com.example.demo.Models.SocialMedia.UserPost;


public interface CartItemRepository extends JpaRepository<CartItem, Long>{
    Optional<CartItem> findByCartAndProduct(Cart cart, UserPost product);

    @Query("SELECT ci FROM CartItem ci WHERE ci.product.id = :productId AND ci.cart.id = :cartId")
    Optional<CartItem> findCartItemByProductIdAndCartId(@Param("productId") Long productId, @Param("cartId") Long cartId);

    @Modifying
    @Query(value = "INSERT INTO cart_items (product_id, quantity, cart_id, deleted) VALUES (:productId, :quantity, :cartId, false)", nativeQuery = true)
    void saveCartItem(@Param("productId") Long productId, @Param("quantity") int quantity, @Param("cartId") Long cartId);

    @Modifying
    @Query(value = "UPDATE cart_items SET quantity = :quantity, deleted = false WHERE cart_id = :cartId", nativeQuery = true)
    void updateDeletedCartItem(@Param("quantity") int quantity, @Param("cartId") Long cartId);


    @Modifying
    @Query(value = "UPDATE cart_items SET quantity = :quantity WHERE cart_id = :cartId", nativeQuery = true)
    void updateExistingCartItem(@Param("quantity") int quantity, @Param("cartId") Long cartId);
}