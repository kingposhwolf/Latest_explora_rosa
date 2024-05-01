package com.example.demo.InputDto.SocialMedia.HashTag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HashTagDto {

    @NotBlank
    @Size(max = 100)
    private String name;
}
