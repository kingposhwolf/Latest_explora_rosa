package com.example.demo.Repositories.UserManagement.AccountManagement;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.Models.UserManagement.User;
import com.example.demo.Models.UserManagement.Management.Role;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    User findByRole(Role role);
}
