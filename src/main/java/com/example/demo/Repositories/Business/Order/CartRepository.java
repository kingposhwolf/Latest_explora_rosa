package com.example.demo.Repositories.Business.Order;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.transaction.Transactional;
import com.example.demo.Models.Ecommerce.Cart.Cart;




public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query(value = "SELECT id FROM cart WHERE customer_id = :customerId AND deleted = false", nativeQuery = true)
    Optional<Long> findCartIdByCustomerId(@Param("customerId") Long customerId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO cart (customer_id, deleted) VALUES (:customerId, false)", nativeQuery = true)
    void saveCart(@Param("customerId") Long customerId);
}
