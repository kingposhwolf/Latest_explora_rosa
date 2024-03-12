package com.example.demo.Repositories;
/*
 * @author Dwight Danda
 *
 */
import com.example.demo.Models.Brand;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends CrudRepository<Brand, Long> {

}
