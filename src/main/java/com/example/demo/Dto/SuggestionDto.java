package com.example.demo.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SuggestionDto {

    @NotBlank
    @Size(max = 100)
    private String name;
}
