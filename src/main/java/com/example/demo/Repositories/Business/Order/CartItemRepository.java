package com.example.demo.Repositories.Business.Order;

import java.util.List;
import java.util.Map;
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

    @Query(value = "SELECT ci.product_id as productId, " +
                "ci.quantity as quantity, " +
                "up.caption as caption, " +
                "GROUP_CONCAT(DISTINCT n.name) as names, " +
                "GROUP_CONCAT(DISTINCT ct.content_types) as contentTypes, " +
                "bp.price as price " +
                "FROM cart_items ci " +
                "JOIN user_posts up ON ci.product_id = up.id " +
                "LEFT JOIN business_post bp ON up.id = bp.id " +
                "JOIN user_post_names n ON up.id = n.user_post_id " +
                "JOIN user_post_content_types ct ON up.id = ct.user_post_id " +
                "WHERE ci.cart_id = :cartId " +
                "GROUP BY ci.product_id", nativeQuery = true)
    List<Map<String, Object>> findCartItemsByCartId(@Param("cartId") Long cartId);
}