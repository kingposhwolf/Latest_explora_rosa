package com.example.demo.Services.BusinessCategoryService;
/*
 * @author Dwight Danda
 *
 */

import org.springframework.http.ResponseEntity;

import com.example.demo.InputDto.BusinessCategoryDto;

public interface BusinessCategoryService {

    ResponseEntity<Object> getAllBusinessCategories();

    ResponseEntity<Object> saveBusinessCategory(BusinessCategoryDto businessCategoryDto);
}
