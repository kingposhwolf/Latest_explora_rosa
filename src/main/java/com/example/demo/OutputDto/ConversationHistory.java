package com.example.demo.OutputDto;

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
}
