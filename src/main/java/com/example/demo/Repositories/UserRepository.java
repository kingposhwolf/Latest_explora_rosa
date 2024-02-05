package com.example.demo.Repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.Models.User;

public interface UserRepository extends CrudRepository<User, Long> {
    // Custom query method to find users by username
    User findByUsername(String username);
}
