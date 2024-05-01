package com.example.demo.OutputDto;

import java.time.LocalDate;

import com.example.demo.chat.MessageStatus;

import lombok.Data;

@Data
public class MessageOutup {
    private Long senderId;

    private Long recipientId;

    private String content;

    private LocalDate timestamp;

    private MessageStatus status;

    private Long id;

    public MessageOutup(Long id, Long senderId, String content, Long recipientId, MessageStatus status, LocalDate timestamp) {
        this.id = id;
        this.senderId = senderId;
        this.content = content;
        this.timestamp = timestamp;
        this.recipientId = recipientId;
        this.status = status;
    }

}
