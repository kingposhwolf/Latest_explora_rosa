package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{
    
}
