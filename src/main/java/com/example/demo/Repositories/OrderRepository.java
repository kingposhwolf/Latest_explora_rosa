package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.Ecommerce.Order.Order;


public interface OrderRepository extends JpaRepository<Order, Long>{
    
}
