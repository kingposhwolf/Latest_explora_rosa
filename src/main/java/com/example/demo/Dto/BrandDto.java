package com.example.demo.Dto;
/*
 * @author Dwight Danda
 *
 */
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BrandDto {

    @NotNull
    private Long id;
}
