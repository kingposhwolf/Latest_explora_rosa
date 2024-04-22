package com.example.demo.InputDto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeleteFavoriteDto {
    @NotNull
    private Long profileId;

    @NotNull
    private Long postId;
}
