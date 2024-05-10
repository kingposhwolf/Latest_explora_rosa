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

import com.example.demo.InputDto.UserManagement.Authentication.JwtAuthenticationResponse;
import com.example.demo.InputDto.UserManagement.Authentication.RefreshTokenRequest;
import com.example.demo.InputDto.UserManagement.Authentication.SignUpRequest;
import com.example.demo.InputDto.UserManagement.Authentication.SigninRequest;
import com.example.demo.InputDto.UserManagement.Profile.RegistrationResponse;
import com.example.demo.Models.UserManagement.Profile;
import com.example.demo.Models.UserManagement.User;
import com.example.demo.Models.UserManagement.BussinessAccount.Brand;
import com.example.demo.Models.UserManagement.BussinessAccount.BusinessCategory;
import com.example.demo.Models.UserManagement.Management.AccountType;
import com.example.demo.Models.UserManagement.Management.Role;
import com.example.demo.Models.UserManagement.Management.VerificationStatus;
import com.example.demo.Models.UserManagement.PersonalAccount.Personal;
import com.example.demo.Repositories.UserManagement.AccountManagement.AccountTypeRepository;
import com.example.demo.Repositories.UserManagement.AccountManagement.ProfileRepository;
import com.example.demo.Repositories.UserManagement.AccountManagement.UserRepository;
import com.example.demo.Repositories.UserManagement.BusinessAccount.BrandRepository;
import com.example.demo.Repositories.UserManagement.BusinessAccount.BusinessCategoryRepository;
import com.example.demo.Services.JWTService.JWTService;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    
    private final UserRepository userRepository;

    private final BrandRepository brandRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final AccountTypeRepository accountTypeRepository;

    private final ProfileRepository profileRepository;

    private final BusinessCategoryRepository businessCategoryRepository;

    private final JWTService jwtService;

    @SuppressWarnings("null")
    @Transactional
    public ResponseEntity<Object> signup(SignUpRequest signUpRequest) {
        try{

    Optional<AccountType> accountType = accountTypeRepository.findById(signUpRequest.getAccountTypeId());

    if (accountType.isEmpty()) {
        String errorMessage = "Invalid input. ";
        
            errorMessage += "Invalid accountTypeId: " + signUpRequest.getAccountTypeId() + ". ";

        logger.error("User registration failed, Validation Error" + errorMessage);
        return ResponseEntity.badRequest().body(errorMessage);

    }else if(accountType.get().getId() != 1 && accountType.get().getId() != 2){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The Account Type you provide is Invalid");
    }else{
            //Check if username and password Exist
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
    user.setAccountType(accountType.get());
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

    if(accountType.get().getName().equalsIgnoreCase("PERSONAL")){
        Personal personal = new Personal();
        personal.setBio("");
        personal.setFollowers(0);
        personal.setFollowing(0);
        personal.setPosts(0);
        personal.setPowerSize(0);
        personal.setVerificationStatus(VerificationStatus.UNVERIFIED);
        personal.setUser(user);
        personal.setCountry(null);
        personal.setTitle(null);
        personal.setVerificationStatus(VerificationStatus.UNVERIFIED);

        Profile profile2 = profileRepository.save(personal);
        logger.info("\nProfile saved Successful:\n" + personal);

        //Return response
        RegistrationResponse registrationResponse = new RegistrationResponse();

        registrationResponse.setJwtAuthenticationResponse(jwtAuthenticationResponse);
        registrationResponse.setProfileId(profile2.getId());
        registrationResponse.setAccountType(user.getAccountType());

        return ResponseEntity.status(201).body(registrationResponse);

    }else {

        //Executed only ifthe business Account is created
    BusinessCategory businessCategory = businessCategoryRepository.findById(signUpRequest.getBusinessCategoryId()).orElse(null);
    // City city = cityRepository.findById((long) 1).orElse(null);

    if(businessCategory == null){
        // if(businessCategory == null){

            logger.error("User Registration Failed, busines category Must be not be Null");
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Business Category Does not exist is null");

        // }else{

        //     logger.error("User Registration Failed, city Must be not be Null");
        //     return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("City Does not exist or is null");
        // }
        
    }else{
        Brand brand = new Brand();
        brand.setCity(null);
        brand.setRates(new BigDecimal(0.0));
        brand.setTinNumber(null);
        brand.setVerificationStatus(VerificationStatus.UNVERIFIED);
        brand.setUser(user);
        brand.setAddress(null);
        brand.setCoverPhoto(null);
        brand.setBio("tell us about your business");
        brand.setBusinessCategories(businessCategory);
        brand.setAddress(null);
        brand.setCoverPhoto(null);
        brand.setFollowers(0);
        brand.setFollowing(0);
        brand.setPowerSize(0);
        brand.setPosts(0);
        brand.setCountry(null);

        Brand brand2 = brandRepository.save(brand);
    logger.info("\nBrand saved Successful:\n" + brand2);

    //Return response
    RegistrationResponse registrationResponse = new RegistrationResponse();

    registrationResponse.setJwtAuthenticationResponse(jwtAuthenticationResponse);
    registrationResponse.setProfileId(brand2.getId());
    registrationResponse.setAccountType(user.getAccountType());

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

        RegistrationResponse registrationResponse = new RegistrationResponse();
        Profile profile = profileRepository.findByUser(user);

        registrationResponse.setJwtAuthenticationResponse(jwtAuthenticationResponse);
        registrationResponse.setProfileId(profile.getId());
        registrationResponse.setAccountType(user.getAccountType());

        logger.info("Login Successful for the user : " + user);
        return ResponseEntity.ok(registrationResponse);

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
