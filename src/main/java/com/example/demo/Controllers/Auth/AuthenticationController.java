package com.example.demo.Controllers.Auth;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Components.GlobalValidationFormatter.GlobalValidationFormatter;
import com.example.demo.Dto.NewPasswordDto;
import com.example.demo.Dto.PasswordResetDto;
import com.example.demo.Dto.RefreshTokenRequest;
import com.example.demo.Dto.SignUpRequest;
import com.example.demo.Dto.SigninRequest;
import com.example.demo.Services.AuthenticationService.AuthenticationService;
import com.example.demo.Services.PasswordResetService.PasswordResetService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth/")
@RequiredArgsConstructor
public class AuthenticationController {
    
    private final AuthenticationService authenticationService;

    private PasswordResetService passwordResetService;

    private GlobalValidationFormatter globalValidationFormatter;

    @PostMapping("/forgot-password")
    public ResponseEntity<Object> forgotPassword(@RequestBody @Valid PasswordResetDto passwordResetDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return passwordResetService.sendPasswordResetToken(passwordResetDto.getEmail());
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(@RequestBody @Valid NewPasswordDto newPasswordDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return passwordResetService.resetPassword(newPasswordDto);
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid SignUpRequest signUpRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return authenticationService.signup(signUpRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid SigninRequest signinRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            globalValidationFormatter.validationFormatter(bindingResult);
        }
        return authenticationService.signin(signinRequest);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Object> refresh(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            globalValidationFormatter.validationFormatter(bindingResult);
        }
        return authenticationService.refreshToken(refreshTokenRequest);
    }
}
