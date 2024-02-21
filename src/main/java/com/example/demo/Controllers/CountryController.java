package com.example.demo.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dto.CountryDto;
import com.example.demo.Models.Country;
import com.example.demo.Services.CountryService.CountryServiceImpl;
import com.example.demo.Services.CountryService.DuplicateCountryException;

@RestController
@RequestMapping("/countries")
public class CountryController {
    
    private final CountryServiceImpl countryServiceImpl;

    public CountryController(CountryServiceImpl countryServiceImpl) {
        this.countryServiceImpl = countryServiceImpl;
    }
    
    @GetMapping("/all")
    public ResponseEntity<Object> getAllCountries() {
        Iterable<Country> countries = countryServiceImpl.getAllCountries();

        if (!countries.iterator().hasNext()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no registered country yet");
        }
        else{
            return ResponseEntity.ok().body(countries); 
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<Object> registerCountry(@RequestBody CountryDto countryDto) {
        try {
            Country savedCountry = countryServiceImpl.saveCountry(countryDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCountry);
        } catch (DuplicateCountryException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Country with the same name already registered");
        }
    }
}
