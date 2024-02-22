package com.example.demo.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProfileDto {
    @NotNull
    Long userId;

    @NotNull
    private String profilePicture;

    @NotNull
    private String bio;
}
