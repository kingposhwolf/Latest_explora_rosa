package com.example.demo.InputDto.UserManagement.Profile;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GetProfileDto {
    @NotNull
    private Long id;
    
}
