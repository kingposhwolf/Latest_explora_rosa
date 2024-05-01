package com.example.demo.InputDto.SocialMedia.Post;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostRetrieveDto {
    @NotNull
    private Long profileId;
}
