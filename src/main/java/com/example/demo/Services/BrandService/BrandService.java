package com.example.demo.Services.BrandService;

import org.springframework.http.ResponseEntity;

import com.example.demo.Dto.GetBrandDto;

public interface BrandService {

    ResponseEntity<Object> getBrandById(GetBrandDto getBrandDto);
}
