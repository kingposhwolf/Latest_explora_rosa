package com.example.demo.Controllers.Personal;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Components.GlobalValidationFormatter.GlobalValidationFormatter;
import com.example.demo.InputDto.UserManagement.Profile.GetProfileDto;
import com.example.demo.InputDto.UserManagement.Profile.ProfileVisitDto;
import com.example.demo.Services.PersonalService.PersonalServiceImpl;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/personal")
@AllArgsConstructor
public class PersonalController {

    private GlobalValidationFormatter globalValidationFormatter;

    private final PersonalServiceImpl profileServiceImpl;


    @PostMapping("/visit")
    public ResponseEntity<Object> profileById(@RequestBody @Valid ProfileVisitDto profileVisitDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return profileServiceImpl.getProfileById(profileVisitDto);
    }

    @PostMapping("/myProfile")
    public ResponseEntity<Object> ownProfileById(@RequestBody @Valid GetProfileDto getProfileDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return profileServiceImpl.getOwnProfileById(getProfileDto);
    }
}
