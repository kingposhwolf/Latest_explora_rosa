package com.example.demo.Controllers.Profile;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Services.ProfileService.ProfileServiceImpl;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/profile")
@AllArgsConstructor
public class ProfileController {
    private final ProfileServiceImpl profileServiceImpl;

    // private GlobalValidationFormatter globalValidationFormatter;

    @PostMapping("/update")
    public ResponseEntity<Object> updateProfile(@RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture,
                                            @RequestParam(value = "profileId", required = false) Long profileId,
                                            @RequestParam(value = "coverPhoto", required = false) MultipartFile coverPhoto,
                                            @RequestParam(value = "bio", required = false) String bio,
                                            @RequestParam(value = "address", required = false) String address) {
        return profileServiceImpl.updateProfile(profilePicture, profileId,coverPhoto,bio,address);
    }
}
