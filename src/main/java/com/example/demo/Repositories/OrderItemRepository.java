package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.Ecommerce.Order.OrderItem;


public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{
    
}
