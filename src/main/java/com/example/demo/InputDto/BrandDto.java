package com.example.demo.InputDto;
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

    private String address;

    private Long cityId;
}
