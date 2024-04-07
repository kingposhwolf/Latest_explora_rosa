package com.example.demo.InputDto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FetchCommentReplyDto {
    @NotNull
    private Long parentId;
}
