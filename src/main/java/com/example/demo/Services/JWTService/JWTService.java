package com.example.demo.Services.JWTService;

import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {
    String extractUserName(String token);

    String generateToken(UserDetails userDetails);

    boolean isTokenValid(String jwt, UserDetails userDetails);
}
