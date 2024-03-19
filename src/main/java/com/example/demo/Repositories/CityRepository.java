package com.example.demo.Repositories;
import com.example.demo.InputDto.CityDto;
/*
 * @author Dwight Danda
 *
 */
import com.example.demo.Models.City;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface CityRepository extends CrudRepository<City,Long> {

    Optional<City> findByName(String name);

    @Query("SELECT new map(c.id as id, c.name as name, c.zipCode as zipCode, c.state as state) FROM City c")
    List<Map<String, CityDto>> findCitiesWithoutCountry();

}
