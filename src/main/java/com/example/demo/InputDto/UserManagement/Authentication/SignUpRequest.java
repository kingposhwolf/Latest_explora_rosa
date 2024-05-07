package com.example.demo.InputDto.UserManagement.Authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {


    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    private Long accountTypeId;

    @NotNull
    @Size(max = 100)
    private String username;

    @NotNull
    @Email
    @Size(max = 100)
    private String email;

    @NotNull
    @Size(max = 128, min = 8)
    private String password;

    private Long businessCategoryId;

    @NotNull
    private Long cityId;

    @NotNull
    private Long countryId;
}
