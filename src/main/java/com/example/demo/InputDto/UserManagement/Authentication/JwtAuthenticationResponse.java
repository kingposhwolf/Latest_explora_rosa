package com.example.demo.InputDto.UserManagement.Authentication;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {
    
    private String token;

    private String refreshToken;
}
