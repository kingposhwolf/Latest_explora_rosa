package com.example.demo.OutputDto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentReplyOutputDto {
    @NotNull
    private Long profileId;

    @NotNull
    private Long id;

    @NotNull
    private Long postId;

    @NotNull
    private String message;

    @NotNull
    private Long parentId;

    @NotNull
    private Long time;
}
