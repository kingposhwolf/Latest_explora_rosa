package com.example.demo.userChat;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.InputDto.ChatUser;
import com.example.demo.Models.UserManagement.User;
import com.example.demo.Models.UserManagement.Management.Status;
import com.example.demo.Repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService2 {
    private final UserRepository repository;

    public void saveUser(ChatUser user) {
        Optional<User> userOptional = repository.findByUsername(user.getUsername());
        if(userOptional.isPresent()){
            User user2 = userOptional.get();
            user2.setStatus(Status.ONLINE);
            repository.save(user2);
        }
    }

    @SuppressWarnings("null")
    public void disconnect(ChatUser user) {
        Optional<User> userOptional = repository.findByUsername(user.getUsername());
        if(userOptional.isPresent()){
            User user2 = userOptional.get();
            user2.setStatus(Status.OFFLINE);
            repository.save(user2);
        }
    }

    public List<User> findConnectedUsers() {
         return repository.findByStatus(Status.ONLINE);
    }
}
