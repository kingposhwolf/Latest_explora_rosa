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
public class AckMessageDto {
    private MessageStatus status;

    private String tempMessageId;

    private Long messageId;
}
