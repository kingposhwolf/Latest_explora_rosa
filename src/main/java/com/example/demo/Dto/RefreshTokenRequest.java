package com.example.demo.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RefreshTokenRequest {
    
    @NotNull
    private String token;
}
