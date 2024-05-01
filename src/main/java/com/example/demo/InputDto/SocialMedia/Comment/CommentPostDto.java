package com.example.demo.InputDto.SocialMedia.Comment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentPostDto {
    @NotNull
    private Long postId;
}
