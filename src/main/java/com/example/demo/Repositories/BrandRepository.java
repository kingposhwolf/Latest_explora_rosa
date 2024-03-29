package com.example.demo.Repositories;
/*
 * @author Dwight Danda
 *
 */
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.UserManagement.BussinessAccount.Brand;


@Repository
public interface BrandRepository extends CrudRepository<Brand, Long> {

}
