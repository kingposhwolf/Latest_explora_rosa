package com.example.demo.Services.CountryService;

import org.springframework.http.ResponseEntity;

import com.example.demo.InputDto.CountryDto;

public interface CountryService {
    ResponseEntity<Object> getAllCountries();

    ResponseEntity<Object> saveCountry(CountryDto countryDto);
}
