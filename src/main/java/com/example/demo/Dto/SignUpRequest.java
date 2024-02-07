package com.example.demo.Dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {

    @NotBlank
    // @Size(max = 50)
    private String firstName;

    @NotNull
    // @Size(max = 50)
    private String secondName;

    @NotNull
    @Column(unique = true)
    // @Size(max = 30)
    private String username;

    @NotNull
    @Email
    // @Column(unique = true)
    // @Size(max = 320)
    private String email;

    @NotNull
    // @Size(max = 128)
    private String password;
}
