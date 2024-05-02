package com.example.demo.InputDto.UserManagement.Authentication;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SigninRequest {
    @NotNull
    private String username;

    @NotNull
    @Size(min = 8)
    private String password;
}
