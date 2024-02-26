package com.example.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dto.GetProfileDto;
import com.example.demo.Services.ProfileService.ProfileServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private GlobalValidationFormatter globalValidationFormatter;

    private final ProfileServiceImpl profileServiceImpl;

    public ProfileController(ProfileServiceImpl profileServiceImpl){
        this.profileServiceImpl = profileServiceImpl;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerCity(@RequestBody @Valid GetProfileDto getProfileDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return profileServiceImpl.getProfileById(getProfileDto);
    }
}
