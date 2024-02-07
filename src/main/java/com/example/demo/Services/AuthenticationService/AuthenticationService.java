package com.example.demo.Services.AuthenticationService;

import com.example.demo.Dto.SignUpRequest;
import com.example.demo.Models.User;

public interface AuthenticationService {
    
    User signup(SignUpRequest signUpRequest);
}
