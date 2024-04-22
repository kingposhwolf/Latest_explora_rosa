package com.example.demo.chat.Dto;

import lombok.Data;

@Data
public class ChatMessageDto {
    private Long senderId;

    private Long recipientId;

    private String content;

    private String tempMessageId;
}
