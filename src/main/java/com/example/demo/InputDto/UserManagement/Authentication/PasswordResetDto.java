package com.example.demo.InputDto.UserManagement.Authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordResetDto {
    @NotNull
    @Email
    @Size(max = 100)
    private String email;
}
