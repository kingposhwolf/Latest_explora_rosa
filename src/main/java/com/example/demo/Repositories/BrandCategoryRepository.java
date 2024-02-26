package com.example.demo.Repositories;
/*
 * @author Dwight Danda
 *
 */
import com.example.demo.Models.BrandCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandCategoryRepository extends CrudRepository<BrandCategory, Long> {

}
