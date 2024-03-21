package com.example.demo.Services.PersonalService;

import org.springframework.http.ResponseEntity;

import com.example.demo.InputDto.ProfileVisitDto;

public interface PersonalService {

    ResponseEntity<Object> getProfileById(ProfileVisitDto profileVisitDto);

}
