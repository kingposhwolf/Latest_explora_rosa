package com.example.demo.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProfileDto {
    @NotNull
    Long id;

    private String profilePicture;

    private String bio;

    private Long countryId;
}
