package com.example.demo.InputDto.SearchDto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SearchDto {
    @NotNull
    private Long profileId;

    @NotNull
    private String keyword;
}
