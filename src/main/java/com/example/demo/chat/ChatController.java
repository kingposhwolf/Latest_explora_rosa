package com.example.demo.chat;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;

import com.example.demo.Models.Chat.ChatHistory;
import com.example.demo.Models.UserManagement.User;
import com.example.demo.Repositories.ChatHistoryRepository;
import com.example.demo.Repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final UserRepository userRepository;
    private final ChatHistoryRepository chatHistoryRepository;

    @SuppressWarnings("null")
    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        try {
            ChatMessage savedMsg = chatMessageService.save(chatMessage);
            messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(), "/queue/messages",
                new ChatNotification(
                    savedMsg.getId(),
                    savedMsg.getSenderId(),
                    savedMsg.getRecipientId(),
                    savedMsg.getContent()
                )
            );

            // Save the message to chat history
            Optional<User> optionalReceiver = userRepository.findByUsername(chatMessage.getRecipientId());
            Optional<ChatHistory> chatHistoryOptional = chatHistoryRepository.findBySenderIdAndRecepient(chatMessage.getSenderId(), optionalReceiver.get());
            if(chatHistoryOptional.isPresent()) {
                
            } else {
                ChatHistory chatHistory = new ChatHistory();
                chatHistory.setSenderId(chatMessage.getSenderId());
                chatHistory.setRecepient(optionalReceiver.get());
                chatHistoryRepository.save(chatHistory);
            }
            
            // Send ACK back to the client using the session from headerAccessor
           // String sessionId = headerAccessor.getSessionId();
            String endpoint = "/topic/ack/"+chatMessage.getSenderId();
            messagingTemplate.convertAndSend(endpoint,
                new TextMessage("ACK")
            );
        } catch (Exception exception) {
            // Properly handle the exception, e.g., log it
            exception.printStackTrace();
        }
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable String senderId,
                                                @PathVariable String recipientId) {
                                                        return ResponseEntity
                .ok(chatMessageService.findChatMessages(senderId, recipientId));
        
    }
}
