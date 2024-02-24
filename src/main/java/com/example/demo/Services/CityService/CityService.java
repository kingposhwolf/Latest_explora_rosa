package com.example.demo.Services.CityService;
/*
 * @author Dwight Danda
 *
 */
import com.example.demo.Dto.CityDto;
import com.example.demo.Models.City;

public interface CityService {

    Iterable<City> getAllCities();

    City saveCity (CityDto cityDto);

}
