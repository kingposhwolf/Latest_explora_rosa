package com.example.demo.Services.AuthenticationService;

import com.example.demo.Dto.JwtAuthenticationResponse;
import com.example.demo.Dto.RefreshTokenRequest;
import com.example.demo.Dto.SignUpRequest;
import com.example.demo.Dto.SigninRequest;
import com.example.demo.Models.User;

public interface AuthenticationService {
    
    User signup(SignUpRequest signUpRequest);

    JwtAuthenticationResponse signin(SigninRequest signinRequest);

    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
