package com.example.demo.InputDto;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {
    
    private String token;

    private String refreshToken;
}
