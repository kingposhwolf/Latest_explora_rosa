package com.example.demo.InputDto.SocialMedia.Comment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FetchCommentReplyDto {
    @NotNull
    private Long parentId;

    @NotNull
    private Long postId;
}
