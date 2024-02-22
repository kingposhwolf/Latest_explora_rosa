package com.example.demo.Dto;

import lombok.Data;

@Data
public class RegistrationResponse {
    private Long profileId;

    private JwtAuthenticationResponse jwtAuthenticationResponse;
}
