package com.example.demo.Services.PersonalService;

import org.springframework.http.ResponseEntity;

import com.example.demo.InputDto.UserManagement.Profile.GetProfileDto;
import com.example.demo.InputDto.UserManagement.Profile.ProfileVisitDto;

public interface PersonalService {

    ResponseEntity<Object> getProfileById(ProfileVisitDto profileVisitDto);

    ResponseEntity<Object> getOwnProfileById(GetProfileDto getProfileDto);

}
