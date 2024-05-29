package com.example.demo.chat.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChatMessageDto {
    @NotNull
    private Long senderId;

    @NotNull
    private Long recipientId;

    @NotNull
    private String content;

    @NotNull
    private String tempMessageId;
}
