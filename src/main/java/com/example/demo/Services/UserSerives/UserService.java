package com.example.demo.Services.UserSerives;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.demo.Models.User;

public interface UserService {
    UserDetailsService userDetailsService();

    User registerUser(User user);
}
