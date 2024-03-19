package com.example.demo.InputDto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewPasswordDto {
    @NotNull
    @Size(min = 8)
    private String password;

    @NotNull
    private String token;
}
