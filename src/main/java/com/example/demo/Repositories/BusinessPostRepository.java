package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.BusinessPost;

public interface BusinessPostRepository extends JpaRepository<BusinessPost, Long>{
    
}
