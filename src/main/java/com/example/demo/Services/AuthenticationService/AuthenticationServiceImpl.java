package com.example.demo.Services.AuthenticationService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.Dto.SignUpRequest;
import com.example.demo.Models.Role;
import com.example.demo.Models.User;
import com.example.demo.Repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public User signup(SignUpRequest signUpRequest){
        User user = new User();

        user.setEmail(signUpRequest.getEmail());
        user.setFirstName(signUpRequest.getFirstName());
        user.setSecondName(signUpRequest.getSecondName());
        user.setUsername(signUpRequest.getUsername());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        return userRepository.save(user);
    }
}
