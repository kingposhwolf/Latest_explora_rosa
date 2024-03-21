package com.example.demo.Services.AuthenticationService;

import org.springframework.http.ResponseEntity;

import com.example.demo.InputDto.RefreshTokenRequest;
import com.example.demo.InputDto.SignUpRequest;
import com.example.demo.InputDto.SigninRequest;

public interface AuthenticationService {
    
    ResponseEntity<Object> signup(SignUpRequest signUpRequest);

    ResponseEntity<Object> signin(SigninRequest signinRequest);

    ResponseEntity<Object> refreshToken(RefreshTokenRequest refreshTokenRequest);
}
