package com.example.demo.Services.BrandService;

import org.springframework.http.ResponseEntity;

import com.example.demo.InputDto.UserManagement.Profile.GetProfileDto;
import com.example.demo.InputDto.UserManagement.Profile.ProfileVisitDto;

public interface BrandService {

    ResponseEntity<Object> getBrandById(ProfileVisitDto getBrandDto);

    ResponseEntity<Object> getOwnBrandById(GetProfileDto getProfileDto);
}
