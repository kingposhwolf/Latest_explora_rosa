package com.example.demo.InputDto.ManagementInfo.Country;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CountryDto {

    @NotBlank
    @Size(max = 100)
    private String name;
}
