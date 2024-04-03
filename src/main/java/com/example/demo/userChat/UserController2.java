package com.example.demo.userChat;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.InputDto.ChatUser;
import com.example.demo.Models.UserManagement.User;
import com.example.demo.Repositories.ChatHistoryRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController2 {
    private final UserService2 userService;
    private final ChatHistoryRepository chatHistoryRepository;

    @MessageMapping("/user.addUser")
    @SendTo("/user/public")
    public ChatUser addUser(
            @Payload ChatUser user
    ) {
        userService.saveUser(user);
        return user;
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/public")
    public ChatUser disconnectUser(
            @Payload ChatUser user
    ) {
        userService.disconnect(user);
        return user;
    }

    @GetMapping("/users/{senderId}")
    public ResponseEntity<List<User>> findConnectedUsers(@PathVariable String senderId) {
    //    return ResponseEntity.ok(userService.findConnectedUsers());
        return ResponseEntity.ok(chatHistoryRepository.findRecepientBySenderId(senderId));
    }
}
