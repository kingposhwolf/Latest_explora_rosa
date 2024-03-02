package com.example.demo.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController2 {
    private final UserService2 userService;

    @MessageMapping("/user.addUser")
    @SendTo("/user/public")
    public User2 addUser(
            @Payload User2 user
    ) {
        userService.saveUser(user);
        return user;
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/public")
    public User2 disconnectUser(
            @Payload User2 user
    ) {
        userService.disconnect(user);
        return user;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User2>> findConnectedUsers() {
        return ResponseEntity.ok(userService.findConnectedUsers());
    }
}
