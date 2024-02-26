package com.example.demo.Services.BrandService;

import org.springframework.http.ResponseEntity;

import com.example.demo.Dto.BrandDto;

public interface BrandService {

    ResponseEntity<Object> getBrandById(BrandDto brandDto);
}
