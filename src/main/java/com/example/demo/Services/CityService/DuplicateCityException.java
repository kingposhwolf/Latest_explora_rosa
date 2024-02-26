package com.example.demo.Services.CityService;

import com.example.demo.Services.CountryService.DuplicateCountryException;

public class DuplicateCityException extends RuntimeException{
    public DuplicateCityException(String message){
        super(message);
    }

}
