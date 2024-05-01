package com.example.demo.Controllers.Profile;

import org.springframework.http.ResponseEntity;
// import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

// import com.example.demo.Components.GlobalValidationFormatter.GlobalValidationFormatter;
// import com.example.demo.InputDto.UserManagement.Profile.ProfileVisitDto;
import com.example.demo.Services.ProfileService.ProfileServiceImpl;

// import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/profile")
@AllArgsConstructor
public class ProfileController {
    private final ProfileServiceImpl profileServiceImpl;

    // private GlobalValidationFormatter globalValidationFormatter;

    @PostMapping("/update")
    public ResponseEntity<Object> profileById(@RequestParam("profilePicture") MultipartFile profilePicture,
                                            @RequestParam(value = "profileId") Long profileId) {
        return profileServiceImpl.updateProfile(profilePicture, profileId);
    }
}
