package com.example.demo.Repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.Models.UserPost;

public interface UserPostRespository extends CrudRepository<UserPost, Long>{
    
}
