package com.example.demo.Services.AuthenticationService;

import java.util.HashMap;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.Dto.JwtAuthenticationResponse;
import com.example.demo.Dto.RefreshTokenRequest;
import com.example.demo.Dto.RegistrationResponse;
import com.example.demo.Dto.SignUpRequest;
import com.example.demo.Dto.SigninRequest;
import com.example.demo.Models.AccountType;
import com.example.demo.Models.Country;
import com.example.demo.Models.Profile;
import com.example.demo.Models.Role;
import com.example.demo.Models.User;
import com.example.demo.Repositories.AccountTypeRepository;
import com.example.demo.Repositories.CountryRepository;
import com.example.demo.Repositories.ProfileRepository;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Services.JWTService.JWTService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    
    private final UserRepository userRepository;

    private final CountryRepository countryRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final AccountTypeRepository accountTypeRepository;

    private final ProfileRepository profileRepository;

    private final JWTService jwtService;

    @SuppressWarnings("null")
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
        //creating new user
        User user = new User();
    user.setEmail(signUpRequest.getEmail());
    user.setName(signUpRequest.getName());
    user.setAccountType(accountType);
    user.setCountry(country);
    user.setUsername(signUpRequest.getUsername());
    user.setRole(Role.USER);
    user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

    userRepository.save(user);
    
    logger.info("User Saved sucessfull" + user);

        //Logged in the User
    var jwt = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(new HashMap<>() ,user);
    JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
    jwtAuthenticationResponse.setToken(jwt);
    jwtAuthenticationResponse.setRefreshToken(refreshToken);

    //Create User Profile
    // Title title = new Title();
    //        title.setId((long) 1);
    //        title.setName("USER");

    Profile profile = new Profile();
        profile.setBio("");
        profile.setFollowers(0);
        profile.setFollowing(0);
        profile.setPosts(0);
        profile.setTitle(null);
        profile.setPowerSize(0);
        profile.setTitle(null);
        profile.setUser(user);
    Profile profile2 = profileRepository.save(profile);
    logger.info("\nProfile saved Successful:\n" + profile);
    //Return response
    RegistrationResponse registrationResponse = new RegistrationResponse();

    registrationResponse.setJwtAuthenticationResponse(jwtAuthenticationResponse);
    registrationResponse.setProfileId(profile2.getId());

    return ResponseEntity.status(201).body(registrationResponse);
    }
        }catch(Exception exception){
            logger.error("\nUser saving failed server side Error: \n " + exception.getMessage());
            return ResponseEntity.status(500).body("There is Problem at Our End");
        }
}


    public ResponseEntity<Object> signin(SigninRequest signinRequest){

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getUsername(), signinRequest.getPassword()));

        User user = userRepository.findByUsername(signinRequest.getUsername()).orElseThrow(() -> new IllegalArgumentException("Invalid Username or Password"));

        var jwt = jwtService.generateToken(user);

        var refreshToken = jwtService.generateRefreshToken(new HashMap<>() ,user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);

        return ResponseEntity.ok(jwtAuthenticationResponse);
        }catch(BadCredentialsException exception){
            logger.error("\nInvalid Username or Password", exception.getMessage());
        return ResponseEntity.status(401).body("Invalid Username or Password");
        }
        catch(Exception exception){
            logger.error("\nLogin Failure Error,server side Error:\n" + exception.getMessage());
            return ResponseEntity.status(500).body("There is Problem at Our End");
        }
        
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
