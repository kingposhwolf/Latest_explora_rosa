package com.example.demo.Controllers.Personal;

import com.example.demo.Controllers.GlobalValidationFormatter.GlobalValidationFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dto.ProfileVisitDto;
import com.example.demo.Services.PersonalService.PersonalServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/personal")
public class PersonalController {
    @Autowired
    private GlobalValidationFormatter globalValidationFormatter;

    private final PersonalServiceImpl profileServiceImpl;

    public PersonalController(PersonalServiceImpl profileServiceImpl){
        this.profileServiceImpl = profileServiceImpl;
    }

    @PostMapping("/visit")
    public ResponseEntity<Object> profileById(@RequestBody @Valid ProfileVisitDto profileVisitDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return profileServiceImpl.getProfileById(profileVisitDto);
    }
}
