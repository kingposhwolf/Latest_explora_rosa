package com.example.demo.Dto;

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
    private Long countryId;

    @NotNull
    private Long accountTypeId;

    @NotNull
    @Size(max = 50)
    private String username;

    @NotNull
    @Email
    @Size(max = 100)
    private String email;

    @NotNull
    @Size(max = 128)
    private String password;
}
