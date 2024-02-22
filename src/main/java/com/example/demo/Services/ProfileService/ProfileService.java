package com.example.demo.Services.ProfileService;

import org.springframework.http.ResponseEntity;

import com.example.demo.Dto.ProfileDto;
import com.example.demo.Models.Profile;

public interface ProfileService {
    Iterable<Profile> getAllProfiles();

    ResponseEntity<Object> saveProfile(ProfileDto profileDto);

    ResponseEntity<Object> getProfileById(Long id);

   // Profile updatProfile(ProfileDto profileDto);
}
