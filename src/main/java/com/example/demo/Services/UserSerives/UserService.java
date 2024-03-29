package com.example.demo.Services.UserSerives;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.demo.Models.UserManagement.User;


public interface UserService {
    UserDetailsService userDetailsService();

    User registerUser(User user);
}
