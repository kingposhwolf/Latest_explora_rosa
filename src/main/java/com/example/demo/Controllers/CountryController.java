package com.example.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dto.CountryDto;
import com.example.demo.Services.CountryService.CountryServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/countries")
public class CountryController {

    @Autowired
    private GlobalValidationFormatter globalValidationFormatter;
    
    private final CountryServiceImpl countryServiceImpl;

    public CountryController(CountryServiceImpl countryServiceImpl) {
        this.countryServiceImpl = countryServiceImpl;
    }
    
    @PostMapping("/all")
    public ResponseEntity<Object> getAllCountries() {
        return countryServiceImpl.getAllCountries();
    }
    
    @PostMapping("/register")
    public ResponseEntity<Object> registerCountry(@RequestBody @Valid CountryDto countryDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return countryServiceImpl.saveCountry(countryDto);
    }
}
