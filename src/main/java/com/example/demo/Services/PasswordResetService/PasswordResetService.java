package com.example.demo.Services.PasswordResetService;

import org.springframework.http.ResponseEntity;

import com.example.demo.InputDto.NewPasswordDto;

public interface PasswordResetService {
    ResponseEntity<Object> sendPasswordResetToken(String email);

    ResponseEntity<Object> resetPassword(NewPasswordDto newPasswordDto);
}
