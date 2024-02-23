package com.example.demo.Repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.Models.PasswordResetToken;

public interface PasswordResetRepository extends CrudRepository<PasswordResetToken, Long>{
    Optional<PasswordResetToken> findByToken(String token);
}
