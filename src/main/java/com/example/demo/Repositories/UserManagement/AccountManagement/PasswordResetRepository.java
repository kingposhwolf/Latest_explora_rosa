package com.example.demo.Repositories.UserManagement.AccountManagement;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.Models.UserManagement.Management.PasswordResetToken;


public interface PasswordResetRepository extends CrudRepository<PasswordResetToken, Long>{
    Optional<PasswordResetToken> findByToken(String token);
}
