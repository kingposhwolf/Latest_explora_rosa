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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CityServiceImpl implements CityService{

    private static final Logger logger = LoggerFactory.getLogger(CityServiceImpl.class);

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    public  CityServiceImpl(CityRepository cityRepository, CountryRepository countryRepository){
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    public ResponseEntity<Object> getAllCities(){
        try {
            Iterable<City> cities = cityRepository.findAll();
            if(!cities.iterator().hasNext()){
                logger.error("\nThere is Request for Fetching All Cities, But Nothing Registered to the database ");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is No  Cities in the Database");
            }else{
                logger.info("\nSuccessful fetched all Cities");
                return ResponseEntity.status(200).body(cities);
            }
        } catch (Exception exception) {
            logger.error("\nFailed to fetch all Cities, Server Error: \n" + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @SuppressWarnings("null")
    @Override
    public ResponseEntity<Object> saveCity(CityDto cityDto){
        try {
            Optional<City> existingCity = cityRepository.findByName(cityDto.getName());

            if(existingCity.isPresent()){
                logger.error("\nFailed to save the city, City Already exists Error");
                return ResponseEntity.status(400).body("This City Already Exists!");
            }
            else{
                Country country = countryRepository.findById(cityDto.getCountyId()).orElse(null);
                if(country == null){
                    logger.error("\nFailed to save the city, The provided Country Doesn't exists Error");
                    return ResponseEntity.status(400).body("The Country you provide does not Exist");
                }
                else{
                    City city = new City();
                city.setCountry(country);
                city.setName(cityDto.getName());
                city.setZipCode(cityDto.getZipCode());
                city.setState(cityDto.getState());
                cityRepository.save(city);

                logger.info("\nSuccessful save the city" + city);
                return ResponseEntity.status(201).body("City Created Successfully");
                }
            }
        } catch (Exception exception) {
            logger.error("\nFailed to save the city, Server Error: \n" + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

}
