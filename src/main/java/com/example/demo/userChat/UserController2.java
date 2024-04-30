package com.example.demo.userChat;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.InputDto.ChatUser;
import com.example.demo.Models.UserManagement.Management.Status;
import com.example.demo.OutputDto.ConversationHistory;
import com.example.demo.Repositories.FollowUnFollowRepository;
import com.example.demo.chatroom.Repository.GroupChatRepository;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class UserController2 {
    private final UserService2 userService;
    private final GroupChatRepository groupChatRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserController2.class);

    private final FollowUnFollowRepository followUnFollowRepository;

    @MessageMapping("/addUser")
    @SendTo("/topic/public")
    public ChatUser addUser(@Payload ChatUser user) {
        try {
            if(user.getStatus() == Status.ONLINE){
                userService.saveUser(user);

                //change message status from send to delivered
            }
        } catch (Exception e) {
            // Handle exception
            // For example, log the error
            e.printStackTrace();
            // Return null or throw a custom exception if necessary
            return null;
        }
        return user;
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/public")
    public ChatUser disconnectUser(@Payload ChatUser user) {
        userService.disconnect(user);
        return user;
    }

    @GetMapping("/users/chat-history/{senderId}")
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

    //we will add suggested user for chatting
    @GetMapping("/suggested/users/{senderId}")
    public ResponseEntity<Object> findActiveUsers(@PathVariable Long senderId) {
        try {
            Optional<List<Map<String, Object>>> followings = followUnFollowRepository.findFollowings(senderId);
            if(followings.isPresent()){
                logger.info("fetch followings successfully");
                return ResponseEntity.ok(followings.get());
            }else{
                Optional<List<Map<String, Object>>> followers = followUnFollowRepository.findFollowers(senderId);
            if(followers.isPresent()){
                logger.info("fetch followers successfully");
                return ResponseEntity.ok(followers.get());
            }else{
                return ResponseEntity.ok(null);
            }
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
}

