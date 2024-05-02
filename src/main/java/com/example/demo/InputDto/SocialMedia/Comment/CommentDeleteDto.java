package com.example.demo.InputDto.SocialMedia.Comment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentDeleteDto {
    @NotNull
    private Long commentId;

    @NotNull
    private Long commenterOrPosterId;

    @NotNull
    private boolean ownPost;
}