package com.example.demo.Dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserPostDto {

    @NotNull
    private Long profileId;

    @NotNull
    private List<Long> hashTags;

    @NotNull
    private String caption;

    private Long brandId;

    @NotNull
    private String path;

    private String thumbnail;
}
