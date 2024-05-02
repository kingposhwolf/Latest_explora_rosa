package com.example.demo.InputDto.Message.GroupChat;

import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NewGroupDto {
    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Set<Long> members = new HashSet<>();
}
