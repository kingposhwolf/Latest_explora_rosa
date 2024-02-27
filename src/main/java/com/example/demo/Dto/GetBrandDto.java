package com.example.demo.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GetBrandDto {
    @NotNull
    private Long id;
}
