package com.example.demo.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatNotification {
    private Long id;
    private String senderId;
    private String recipientId;
    private String content;
}
