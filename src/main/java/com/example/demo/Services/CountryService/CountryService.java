package com.example.demo.Services.CountryService;

import com.example.demo.Dto.CountryDto;
import com.example.demo.Models.Country;

public interface CountryService {
    Iterable<Country> getAllCountries();

    Country saveCountry(CountryDto countryDto);
}
