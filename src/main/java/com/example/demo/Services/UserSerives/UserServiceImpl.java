package com.example.demo.Services.UserSerives;


import org.springframework.stereotype.Service;

import com.example.demo.Models.User;
import com.example.demo.Repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @SuppressWarnings("null")
    @Override
    public
    User registerUser(User user) {
        //Check if the username is already taken
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username is already taken");
        }

        // Encrypt the password before saving
       // user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save the user to the database
        return userRepository.save(user);
    }
}
