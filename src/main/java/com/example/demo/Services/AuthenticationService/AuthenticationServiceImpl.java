package com.example.demo.Services.AuthenticationService;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.hibernate.exception.ConstraintViolationException;

import com.example.demo.Dto.JwtAuthenticationResponse;
import com.example.demo.Dto.RefreshTokenRequest;
import com.example.demo.Dto.SignUpRequest;
import com.example.demo.Dto.SigninRequest;
import com.example.demo.Models.AccountType;
import com.example.demo.Models.Country;
import com.example.demo.Models.Role;
import com.example.demo.Models.User;
import com.example.demo.Repositories.AccountTypeRepository;
import com.example.demo.Repositories.CountryRepository;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Services.JWTService.JWTService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    
    private final UserRepository userRepository;

    private final CountryRepository countryRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final AccountTypeRepository accountTypeRepository;

    private final JWTService jwtService;

    public ResponseEntity<Object> signup(SignUpRequest signUpRequest) {
        try{
            Country country = countryRepository.findById(signUpRequest.getCountryId()) .orElse(null);

    AccountType accountType = accountTypeRepository.findById(signUpRequest.getAccountTypeId()).orElse(null);

    if (country == null || accountType == null) {
        String errorMessage = "Invalid input. ";
        if (country == null) {
            errorMessage += "Invalid countryId: " + signUpRequest.getCountryId() + ". ";
        }
        if (accountType == null) {
            errorMessage += "Invalid accountTypeId: " + signUpRequest.getAccountTypeId() + ". ";
        }
        return ResponseEntity.badRequest().body(errorMessage);
    }

    Optional<User> userExist1 = userRepository.findByUsername(signUpRequest.getUsername());
    Optional<User> userExist2 = userRepository.findByEmail(signUpRequest.getEmail());
    if (userExist1.isPresent()) {
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username must be unique.");
    }else if(userExist2.isPresent()){
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("email must be unique.");
    }
    else {
        User user = new User();
    user.setEmail(signUpRequest.getEmail());
    user.setName(signUpRequest.getName());
    user.setAccountType(accountType);
    user.setCountry(country);
    user.setUsername(signUpRequest.getUsername());
    user.setRole(Role.USER);
    user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

    userRepository.save(user);

    return ResponseEntity.ok("message: "+"User saved successfully");
    }
        }catch(Exception exception){
            return ResponseEntity.status(500).body(exception.getMessage());
        }
}


    public JwtAuthenticationResponse signin(SigninRequest signinRequest){
        
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getUsername(), signinRequest.getPassword()));

        User user = userRepository.findByUsername(signinRequest.getUsername()).orElseThrow(() -> new IllegalArgumentException("Invalid Username or Password"));

        var jwt = jwtService.generateToken(user);

        var refreshToken = jwtService.generateRefreshToken(new HashMap<>() ,user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);

        return jwtAuthenticationResponse;
    }

    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest){
        String username = jwtService.extractUserName(refreshTokenRequest.getToken());

        User user = userRepository.findByUsername(username).orElseThrow();

        if(jwtService.isTokenValid(refreshTokenRequest.getToken(), user)){
            var jwt = jwtService.generateToken(user);

            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());

            return jwtAuthenticationResponse;
        }

        return null;
    }
}
