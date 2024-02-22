package com.example.demo.Dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TitleDto {

    @NotNull
    @Size(min = 2, max = 100)
    private String name;
    
}
