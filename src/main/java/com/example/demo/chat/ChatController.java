package com.example.demo.chat;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.socket.TextMessage;

import com.example.demo.chat.Dto.AckMessageDto;
import com.example.demo.chat.Dto.ChatMessageDto;
import com.example.demo.chat.Dto.ChatNotification;
import com.example.demo.chat.Dto.MessageStatusDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessageDto chatMessage) {
        try {
            ChatMessage savedMsg = chatMessageService.save(chatMessage);

            messagingTemplate.convertAndSendToUser(
                String.valueOf(chatMessage.getSenderId()), "/queue/ack", new AckMessageDto(MessageStatus.SENT, chatMessage.getTempMessageId(), savedMsg.getId())
        );
            
            messagingTemplate.convertAndSendToUser(
                String.valueOf(chatMessage.getRecipientId()), "/queue/messages",
                new ChatNotification(
                        savedMsg.getId(),
                        savedMsg.getSender().getId(),
                        savedMsg.getRecipient().getId(),
                        savedMsg.getContent(),
                        savedMsg.getStatus()
                )
            );
        } catch (Exception exception) {
            
            exception.printStackTrace();
        }
    }

    @MessageMapping("/status")
    public void messageStatusUpdate(@Payload MessageStatusDto messageStatusDto) {
        try {
            ChatMessage chat = chatMessageService.updateStatus(messageStatusDto.getMessageId(), messageStatusDto.getStatus());
            messagingTemplate.convertAndSendToUser(
                String.valueOf(chat.getSender().getId()), "/queue/status",messageStatusDto
        );
        } catch (Exception exception) {
            
            exception.printStackTrace();
        }
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<Map<String, Object>>> findChatMessages(@PathVariable Long senderId,
                                                @PathVariable Long recipientId) {
                                                        return ResponseEntity
                .ok(chatMessageService.findChatMessages(senderId, recipientId));
        
    }
}
