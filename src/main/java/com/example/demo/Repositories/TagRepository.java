package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
    
}
