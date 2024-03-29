package com.example.demo.Repositories;
/*
 * @author Dwight Danda
 *
 */
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.UserManagement.BussinessAccount.BusinessCategory;

import java.util.Optional;

@Repository
public interface BusinessCategoryRepository extends CrudRepository<BusinessCategory, Long> {
    Optional<BusinessCategory> findByName(String name);
}
