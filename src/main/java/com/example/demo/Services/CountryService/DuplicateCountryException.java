package com.example.demo.Services.CountryService;

public class DuplicateCountryException extends RuntimeException {

    public DuplicateCountryException(String message) {
        super(message);
    }
}
