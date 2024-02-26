package com.example.demo.Dto;
/*
 * @author Dwight Danda
 *
 */
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CityDto {

    @NotNull
    private Long countyId;

    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    @Size(max = 150)
    private String zipCode;

    @NotNull
    @Size(max = 150)
    private String state;
}
