package com.example.demo.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.InputDto.ChatUser;
import com.example.demo.Models.UserManagement.User;
import com.example.demo.Models.UserManagement.Management.Role;
import com.example.demo.Models.UserManagement.Management.Status;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    User findByRole(Role role);

    // @Query("SELECT new com.example.demo.InputDto.ChatUser(p.user.username, p.user.name, p.status) " +
    //         "FROM Profile p " +
    //         "WHERE p.status = :status")
    // List<ChatUser> findAllByStatus(@Param("status") Status status);
    List<User> findByStatus(Status status);
}
