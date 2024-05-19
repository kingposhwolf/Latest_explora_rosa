package com.example.demo.Services.CountryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Optional;

import com.example.demo.InputDto.ManagementInfo.Country.CountryDto;
import com.example.demo.Models.Information.Country;
import com.example.demo.Repositories.Information.Country.CountryRepository;

import lombok.AllArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CountryServiceImpl implements CountryService {

    private static final Logger logger = LoggerFactory.getLogger(CountryServiceImpl.class);

    private final CountryRepository countryRepository;


    @Override
    public ResponseEntity<Object> getAllCountries() {
        try {
            Iterable<Country> countries = countryRepository.findAll();

            if(!countries.iterator().hasNext()){
                logger.error("\nThere is Request for Fetching All Countries, But Nothing Registered to the database ");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is No  Countries registered in the Database");
            }else{
                logger.info("\nSuccessful fetched all Coiuntries");
                return ResponseEntity.status(200).body(countries);
            }
        } catch (Exception exception) {
            logger.error("\nFailed to fetch Countries, Server Error: \n" + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @Transactional
    @Override
    public ResponseEntity<Object> saveCountry(CountryDto countryDto) {

        try {
            Optional<Country> existingCountry = countryRepository.findByName(countryDto.getName());

        if(existingCountry.isPresent()){
            logger.error("\nFailed to save Business Country, Country Already Exists Error");
            return ResponseEntity.status(400).body("Country Already Exists!");
        }
        else{
            Country country = new Country();
            country.setName(countryDto.getName());
            countryRepository.save(country);

            logger.info("Country saved Sucessfully\n" + country);
            return ResponseEntity.status(201).body("Country Created Successful ");
        }
        } catch (Exception exception) {
            logger.error("\nFailed to save Country, Server Error: \n" + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
    
}
