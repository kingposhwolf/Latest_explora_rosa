package com.example.demo.user;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService2 {
    private final UserRepository2 repository;

    public void saveUser(User2 user) {
        user.setStatus(Status2.ONLINE);
        repository.save(user);
    }

    @SuppressWarnings("null")
    public void disconnect(User2 user) {
        var storedUser = repository.findById(user.getNickName()).orElse(null);
        if (storedUser != null) {
            storedUser.setStatus(Status2.OFFLINE);
            repository.save(storedUser);
        }
    }

    public List<User2> findConnectedUsers() {
        return repository.findAllByStatus(Status2.ONLINE);
    }
}
