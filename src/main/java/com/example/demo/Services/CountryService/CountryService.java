package com.example.demo.Services.CountryService;

import org.springframework.http.ResponseEntity;

import com.example.demo.Dto.CountryDto;

public interface CountryService {
    ResponseEntity<Object> getAllCountries();

    ResponseEntity<Object> saveCountry(CountryDto countryDto);
}
