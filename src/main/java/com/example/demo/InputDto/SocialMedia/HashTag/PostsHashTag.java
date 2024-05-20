package com.example.demo.InputDto.SocialMedia.HashTag;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostsHashTag {
    @NotNull
    private Long hashTagId;

    @NotNull
    private Long profileId;
}
