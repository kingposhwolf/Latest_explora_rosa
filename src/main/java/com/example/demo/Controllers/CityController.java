package com.example.demo.Controllers;

import com.example.demo.Dto.CityDto;
import com.example.demo.Dto.CountryDto;
import com.example.demo.Models.City;
import com.example.demo.Models.Country;
import com.example.demo.Services.CityService.CityServiceImpl;
import com.example.demo.Services.CityService.DuplicateCityException;
import com.example.demo.Services.CountryService.DuplicateCountryException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/cities")
public class CityController {

    private final CityServiceImpl cityServiceImpl;

    public CityController(CityServiceImpl cityServiceImpl){
        this.cityServiceImpl = cityServiceImpl;
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllCities(){
        Iterable<City> cities = cityServiceImpl.getAllCities();

        if (!cities.iterator().hasNext()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no registered country yet");
        }
        else{
            return ResponseEntity.ok().body(cities);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerCity(@RequestBody @Valid CityDto cityDto) {
        try {
            City savedCity = cityServiceImpl.saveCity(cityDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCity);
        } catch (DuplicateCityException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Country with the same name already registered");
        }
    }
}
