package com.example.demo.InputDto.UserManagement.Profile;

import com.example.demo.InputDto.UserManagement.Authentication.JwtAuthenticationResponse;
import com.example.demo.Models.UserManagement.Management.AccountType;

import lombok.Data;

@Data
public class RegistrationResponse {
    private Long profileId;

    private AccountType accountType;

    private JwtAuthenticationResponse jwtAuthenticationResponse;
}
