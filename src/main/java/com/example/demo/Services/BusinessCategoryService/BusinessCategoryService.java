package com.example.demo.Services.BusinessCategoryService;
/*
 * @author Dwight Danda
 *
 */

import com.example.demo.Dto.BusinessCategoryDto;
import org.springframework.http.ResponseEntity;

public interface BusinessCategoryService {

    ResponseEntity<Object> getAllBusinessCategories();

    ResponseEntity<Object> saveBusinessCategory(BusinessCategoryDto businessCategoryDto);
}
