package com.example.demo.OutputDto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SocialMediaInstantChange {
    @NotNull
    private Long postId;

    @NotNull
    private String interact;

    @NotNull
    private int newNumber;
}
