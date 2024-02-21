package com.example.demo.Repositories;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.Models.Country;

public interface CountryRepository extends CrudRepository<Country, Long>{
    Optional<Country> findByName(String name);
}
