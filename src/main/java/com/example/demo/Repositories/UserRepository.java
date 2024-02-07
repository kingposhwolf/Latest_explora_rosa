package com.example.demo.Repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.Models.Role;
import com.example.demo.Models.User;

public interface UserRepository extends CrudRepository<User, Long> {
    // Custom query method to find users by username
    Optional<User> findByUsername(String username);

    User findByRole(Role role);
}
