package com.example.demo.Services.BusinessCategoryService;
/*
 * @author Dwight Danda
 *
 */
import com.example.demo.Dto.BusinessCategoryDto;
import com.example.demo.Models.BusinessCategory;
import com.example.demo.Repositories.BusinessCategoryRepository;
import com.example.demo.Services.AuthenticationService.AuthenticationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BusinessCategoryServiceImp implements BusinessCategoryService {

    private static final Logger logger = LoggerFactory.getLogger(BusinessCategoryServiceImp.class);

    private final BusinessCategoryRepository businessCategoryRepository;
    public BusinessCategoryServiceImp(BusinessCategoryRepository businessCategoryRepository){
        this.businessCategoryRepository = businessCategoryRepository;
    }

    @Override
    public ResponseEntity<Object> getAllBusinessCategories(){
        try{
            logger.info("\nSuccessful fetched all Business Category");
            return ResponseEntity.status(200).body(businessCategoryRepository.findAll());
        }catch (Exception exception){
            logger.error("\nFailed to fetch all Business Category, Server Error: \n" + exception.getMessage());
            return ResponseEntity.status(500).body("\nSomething is wrong at Our End");
        }
    }

    @Override
    public ResponseEntity<Object> saveBusinessCategory(BusinessCategoryDto businessCategoryDto){
        Optional<BusinessCategory> existingBusinessCategory = businessCategoryRepository.findByName(businessCategoryDto.getName());

        if(existingBusinessCategory.isPresent()){
            return ResponseEntity.status(400).body("This Business Category Already Exists!");
        }
        else{
            BusinessCategory businessCategory = new BusinessCategory();
            businessCategory.setName(businessCategoryDto.getName());
            businessCategoryRepository.save(businessCategory);
            return ResponseEntity.status(201).body("Business Category Created Successful ");
        }
    }




}
