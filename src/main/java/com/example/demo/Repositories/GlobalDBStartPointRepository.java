package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.GlobalDBStartPoint;

public interface GlobalDBStartPointRepository extends JpaRepository<GlobalDBStartPoint, Long>{
    Long findHotById(Long id);
}
