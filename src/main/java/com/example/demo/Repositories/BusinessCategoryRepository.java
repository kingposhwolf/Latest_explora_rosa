package com.example.demo.Repositories;
/*
 * @author Dwight Danda
 *
 */
import com.example.demo.Models.BusinessCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessCategoryRepository extends CrudRepository<BusinessCategory, Long> {
    Optional<BusinessCategory> findByName(String name);
}
