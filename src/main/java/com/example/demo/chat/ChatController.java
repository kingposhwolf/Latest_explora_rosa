package com.example.demo.chat;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.chat.Dto.AckMessageDto;
import com.example.demo.chat.Dto.ChatMessageDto;
import com.example.demo.chat.Dto.ChatNotification;
import com.example.demo.chat.Dto.MessageStatusDto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class ChatController {
    // private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    // private final ObjectMapper objectMapper;

@PostMapping("/message/send")
public void processMessage(@RequestBody @Valid ChatMessageDto chatMessage) {
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


    @PostMapping("/message/status")
    public void messageStatusUpdate(@Payload @Valid MessageStatusDto messageStatusDto) {
        try {
            ChatMessage chat = chatMessageService.updateStatus(messageStatusDto.getMessageId(), messageStatusDto.getStatus());
            messagingTemplate.convertAndSendToUser(
                String.valueOf(chat.getSender().getId()), "/queue/status",messageStatusDto
        );
        } catch (Exception exception) {
            
            exception.printStackTrace();
        }
    }

    @PostMapping("/message/group-chat")
    public void sendMessageToGroups(@RequestBody @Valid ChatMessageDto chatMessage) {
        try {
            GroupChatMessage savedMsg = chatMessageService.groupMsgSave(chatMessage);

            messagingTemplate.convertAndSendToUser(
                String.valueOf(chatMessage.getSenderId()), "/queue/ack", new AckMessageDto(MessageStatus.SENT, chatMessage.getTempMessageId(), savedMsg.getId())
        );

            String endpoint = "/topic/groupchat/" + Long.toString(chatMessage.getRecipientId());

                    messagingTemplate.convertAndSend(endpoint, new ChatNotification(
                        savedMsg.getId(),
                        savedMsg.getSender().getId(),
                        savedMsg.getRecipient().getId(),
                        savedMsg.getContent(),
                        savedMsg.getStatus()
                ));
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

    @GetMapping("/group/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<Map<String, Object>>> findGroupChatMessages(@PathVariable Long senderId, @PathVariable Long recipientId) {
        return ResponseEntity.ok(chatMessageService.findGroupChatMessages(senderId, recipientId));
    }
}
