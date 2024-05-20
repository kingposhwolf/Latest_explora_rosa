package com.example.demo.InputDto.SearchDto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SearchHashTagDto {
    @NotNull
    private String keyword;
}
