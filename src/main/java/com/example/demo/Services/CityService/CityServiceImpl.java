package com.example.demo.Services.CityService;
/*
 * @author Dwight Danda
 *
 */
import com.example.demo.Dto.CityDto;
import com.example.demo.Models.City;
import com.example.demo.Models.Country;
import com.example.demo.Repositories.CityRepository;
import com.example.demo.Repositories.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CityServiceImpl implements CityService{

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    public  CityServiceImpl(CityRepository cityRepository, CountryRepository countryRepository){
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
    }



    //Returning a list of all city through Iterable dataType
    @Override
    public Iterable<City> getAllCities(){
        return cityRepository.findAll();
    }

    // Saving a new city
    @Override
    public City saveCity(CityDto cityDto){
        // Creating an object of City class to find out existing city from the repository through condition statements
        Optional<City> existingCity = cityRepository.findByName(cityDto.getName());

        // Throwing Exception for existing city name
        if(existingCity.isPresent()){
            throw new DuplicateCityException("The City with that Name Already Exists!");
        }
        else{
            //Creating an instance of City Class to save all the information to register a city
            City city = new City();

            // Assuming you have access to the countryId in the CityDto
            // Set the country for the city
            Country country = countryRepository.findById(cityDto.getCountyId()).orElseThrow(() -> new RuntimeException("Country not found")); // handle appropriately
            city.setCountry(country);
            city.setName(cityDto.getName());
            city.setZipCode(cityDto.getZipCode());
            city.setState(cityDto.getState());
            return cityRepository.save(city);

        }
    }

}
