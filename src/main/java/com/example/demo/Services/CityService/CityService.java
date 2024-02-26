package com.example.demo.Services.CityService;
import org.springframework.http.ResponseEntity;

/*
 * @author Dwight Danda
 *
 */
import com.example.demo.Dto.CityDto;

public interface CityService {

    ResponseEntity<Object> getAllCities();

    ResponseEntity<Object> saveCity (CityDto cityDto);

}
