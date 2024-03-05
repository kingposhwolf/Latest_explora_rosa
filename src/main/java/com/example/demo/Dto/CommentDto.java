package com.example.demo.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentDto {

    @NotNull
    private Long id;

    @NotNull
    private Long profileId;

    @NotNull
    private String message;
}
