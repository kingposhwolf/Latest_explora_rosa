package com.example.demo.InputDto.UserManagement.Profile;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProfileDto {
    @NotNull
    Long id;

    private String profilePicture;

    private String coverPhoto;

    private String bio;

    private Long countryId;
}
