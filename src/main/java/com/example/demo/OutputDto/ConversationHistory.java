package com.example.demo.OutputDto;

import java.util.Map;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConversationHistory {
    @NotNull
    private Long profileId;

    @NotNull
    private String username;

    @NotNull
    private String name;

    @NotNull
    private Long unreadMessages;

    @NotNull
    private String profilePicture;

    @NotNull
    private String verificationStatus;

    Map<String, Object> lastMessage;
}
