package com.example.demo.Services.CountryService;

import org.springframework.http.ResponseEntity;

import com.example.demo.InputDto.ManagementInfo.Country.CountryDto;

public interface CountryService {
    ResponseEntity<Object> getAllCountries();

    ResponseEntity<Object> saveCountry(CountryDto countryDto);
}
