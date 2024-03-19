package com.example.demo.Services.CityService;
import org.springframework.http.ResponseEntity;

import com.example.demo.InputDto.CityDto;

public interface CityService {

    ResponseEntity<Object> getAllCities();

    ResponseEntity<Object> saveCity (CityDto cityDto);

}
