package com.example.demo.chat.Dto;

import com.example.demo.chat.MessageStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageStatusDto {
    @NotNull
    private MessageStatus status;

    @NotNull
    private Long messageId;
}
