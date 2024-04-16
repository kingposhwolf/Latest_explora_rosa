package com.example.demo.userChat;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
// import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.InputDto.ChatUser;
import com.example.demo.OutputDto.ConversationHistory;
import com.example.demo.chatroom.Repository.GroupChatRepository;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController2 {
    private final UserService2 userService;
    private final GroupChatRepository groupChatRepository;

    @MessageMapping("/user.addUser")
    @SendTo("/user/public")
    public ChatUser addUser(@Payload ChatUser user) {
        userService.saveUser(user);
        return user;
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/public")
    public ChatUser disconnectUser(@Payload ChatUser user) {
        userService.disconnect(user);
        return user;
    }

    @GetMapping("/users/{senderId}")
    public ResponseEntity<Object> findUserConversations(@PathVariable Long senderId) {
        try {
            if(senderId != null){
                List<ConversationHistory> conversations = userService.chatList(senderId);
                return ResponseEntity.ok(conversations);
            }else{
                return ResponseEntity.status(400).body("The user profile id must not be null");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something Went Wrong at our End");
        }
    }

    @GetMapping("/groups/{senderId}")
    public ResponseEntity<Object> findGroupConversations(@PathVariable @NotNull Long senderId) {
        try {
            if(senderId != null){
                List<Map<String, Object>> groupChats = groupChatRepository.findProfileSummaryByMemberId(senderId).get();
                return ResponseEntity.ok(groupChats);
            }else{
                return ResponseEntity.status(400).body("The user profile id must not be null");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something Went Wrong at our End");
        }
    }

    // @ExceptionHandler(Exception.class)
    // public ResponseEntity<String> handleException(Exception e) {
    //     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
    // }
}

