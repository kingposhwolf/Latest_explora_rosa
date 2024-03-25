package com.example.demo.OutputDto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserPostPathDto {
    @NotNull
    private Long postId;


    @NotNull
    private String [] userPostPathDto;
}
