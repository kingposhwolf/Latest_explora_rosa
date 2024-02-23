package com.example.demo.Services.PasswordResetService;

import org.springframework.http.ResponseEntity;

import com.example.demo.Dto.NewPasswordDto;

public interface PasswordResetService {
    ResponseEntity<String> sendPasswordResetToken(String email);

    ResponseEntity<String> resetPassword(NewPasswordDto newPasswordDto);
}
