package com.example.demo.Services.CountryService;

import org.springframework.stereotype.Service;
import java.util.Optional;

import com.example.demo.Dto.CountryDto;
import com.example.demo.Models.Country;
import com.example.demo.Repositories.CountryRepository;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public Iterable<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    @Override
    public Country saveCountry(CountryDto countryDto) {

        Optional<Country> existingCountry = countryRepository.findByName(countryDto.getName());

        if (existingCountry.isPresent()) {
            
            throw new DuplicateCountryException("Country with the same name already exists");
        }
        Country country = new Country();
        country.setName(countryDto.getName());
        return countryRepository.save(country);
    }
    
}
