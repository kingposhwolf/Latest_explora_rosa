package com.example.demo.Services.AuthenticationService;

import org.springframework.http.ResponseEntity;

import com.example.demo.Dto.RefreshTokenRequest;
import com.example.demo.Dto.SignUpRequest;
import com.example.demo.Dto.SigninRequest;

public interface AuthenticationService {
    
    ResponseEntity<Object> signup(SignUpRequest signUpRequest);

    ResponseEntity<Object> signin(SigninRequest signinRequest);

    ResponseEntity<Object> refreshToken(RefreshTokenRequest refreshTokenRequest);
}
