package com.example.demo.Services.BrandService;

import org.springframework.http.ResponseEntity;

import com.example.demo.Dto.ProfileVisitDto;

public interface BrandService {

    ResponseEntity<Object> getBrandById(ProfileVisitDto getBrandDto);
}
