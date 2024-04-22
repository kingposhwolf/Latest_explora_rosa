package com.example.demo.chat.Dto;

import com.example.demo.chat.MessageStatus;

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

    private Long senderId;

    private Long recipientId;

    private String content;

    private MessageStatus status;
}
