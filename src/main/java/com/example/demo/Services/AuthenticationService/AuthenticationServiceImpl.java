package com.example.demo.Services.AuthenticationService;

import java.math.BigDecimal;
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
import com.example.demo.Models.Brand;
import com.example.demo.Models.BusinessCategory;
import com.example.demo.Models.City;
import com.example.demo.Models.Personal;
import com.example.demo.Models.Profile;
import com.example.demo.Models.Role;
import com.example.demo.Models.User;
import com.example.demo.Models.VerificationStatus;
import com.example.demo.Repositories.AccountTypeRepository;
import com.example.demo.Repositories.BrandRepository;
import com.example.demo.Repositories.BusinessCategoryRepository;
import com.example.demo.Repositories.CityRepository;
import com.example.demo.Repositories.ProfileRepository;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Services.JWTService.JWTService;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    
    private final UserRepository userRepository;

    private final BrandRepository brandRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final AccountTypeRepository accountTypeRepository;

    private final ProfileRepository profileRepository;

    private final CityRepository cityRepository;

    private final BusinessCategoryRepository businessCategoryRepository;

    private final JWTService jwtService;

    @SuppressWarnings("null")
    @Transactional
    public ResponseEntity<Object> signup(SignUpRequest signUpRequest) {
        try{

    AccountType accountType = accountTypeRepository.findById(signUpRequest.getAccountTypeId()).orElse(null);

    if (accountType == null) {
        String errorMessage = "Invalid input. ";
        if (accountType == null) {
            errorMessage += "Invalid accountTypeId: " + signUpRequest.getAccountTypeId() + ". ";
        }
        logger.error("User registration failed, Validation Error" + errorMessage);
        return ResponseEntity.badRequest().body(errorMessage);
    }else if(!accountType.getName().equalsIgnoreCase("PERSONAL") || accountType.getName().equalsIgnoreCase("BUSINESS")){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The Account Type you provide is Invalid");
    }else{
        Optional<User> userExist1 = userRepository.findByUsername(signUpRequest.getUsername());
    Optional<User> userExist2 = userRepository.findByEmail(signUpRequest.getEmail());
    if (userExist1.isPresent()) {
        logger.error("User Registration Failed, Username Must be unique Error");
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username must be unique.");
    }else if(userExist2.isPresent()){
        logger.error("User Registration Failed, email Must be unique Error");
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("email must be unique.");
    }
    else {
        //creating new user
        User user = new User();
    user.setEmail(signUpRequest.getEmail());
    user.setName(signUpRequest.getName());
    user.setAccountType(accountType);
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

    if(accountType.getName().equalsIgnoreCase("PERSONAL")){
        Personal personal = new Personal();
        personal.setBio("");
        personal.setFollowers(0);
        personal.setFollowing(0);
        personal.setPosts(0);
        personal.setPowerSize(0);
        personal.setUser(user);
        personal.setCountry(null);
        personal.setTitle(null);

    Profile profile2 = profileRepository.save(personal);
    logger.info("\nProfile saved Successful:\n" + personal);

    //Return response
    RegistrationResponse registrationResponse = new RegistrationResponse();

    registrationResponse.setJwtAuthenticationResponse(jwtAuthenticationResponse);
    registrationResponse.setProfileId(profile2.getId());

    return ResponseEntity.status(201).body(registrationResponse);
    }else {
        BusinessCategory businessCategory = businessCategoryRepository.findById(signUpRequest.getBusinessCategoryId()).orElse(null);
        City city = cityRepository.findById(signUpRequest.getCityId()).orElse(null);
    if(businessCategory == null || city == null){
        if(businessCategory == null){
            logger.error("User Registration Failed, busines category Must be not be Null");
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Business Category Does not exist is null");
        }else{
            logger.error("User Registration Failed, city Must be not be Null");
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("City Does not exist or is null");
        }
        
    }else{
        Brand brand = new Brand();
        brand.setCity(city);
        brand.setRates(new BigDecimal(0.0));
        brand.setTinNumber(null);
        brand.setVerificationStatus(VerificationStatus.UNVERIFIED);
        brand.setUser(user);
        brand.setAddress(null);
        brand.setCoverPhoto(null);

        Brand brand2 = brandRepository.save(brand);
    logger.info("\nBrand saved Successful:\n" + brand2);

    //Return response
    RegistrationResponse registrationResponse = new RegistrationResponse();

    registrationResponse.setJwtAuthenticationResponse(jwtAuthenticationResponse);
    registrationResponse.setProfileId(brand2.getId());

    return ResponseEntity.status(201).body(registrationResponse);
    }
    }
    
    }
    }
        }catch(Exception exception){
            logger.error("\nUser saving failed server side Error: \n " + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
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

        logger.info("Login Successful for the user : " + user);
        return ResponseEntity.ok(jwtAuthenticationResponse);

        }catch(BadCredentialsException exception){
            logger.error("\nInvalid Username or Password", exception.getMessage());
            return ResponseEntity.status(401).body("Invalid Username or Password");
        }
        catch(Exception exception){
            logger.error("\nLogin Failure Error,server side Error:\n" + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
        
    }

    public ResponseEntity<Object> refreshToken(RefreshTokenRequest refreshTokenRequest){
        try {
            String username = jwtService.extractUserName(refreshTokenRequest.getToken());

            User user = userRepository.findByUsername(username).orElseThrow();

        if(jwtService.isTokenValid(refreshTokenRequest.getToken(), user)){
            var jwt = jwtService.generateToken(user);

            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());

            logger.info("New Token Generated Successful from Refresh token for the user : " + user);
            return ResponseEntity.ok(jwtAuthenticationResponse);
        }else{
            logger.error("\nInvalid Token Error for user : \n" + user);
            return ResponseEntity.status(403).body("Invalid Refresh Token");
        }
        } catch (Exception exception) {
            logger.error("\nLogin Failure Error,server side Error:\n" + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server error");
        }
        
    }
}
