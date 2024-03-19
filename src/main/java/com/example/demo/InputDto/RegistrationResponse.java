package com.example.demo.InputDto;

import lombok.Data;

@Data
public class RegistrationResponse {
    private Long profileId;

    private JwtAuthenticationResponse jwtAuthenticationResponse;
}
