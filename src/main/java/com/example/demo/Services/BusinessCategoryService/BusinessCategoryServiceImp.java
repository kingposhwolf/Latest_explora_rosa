package com.example.demo.Services.BusinessCategoryService;
/*
 * @author Dwight Danda
 *
 */
import com.example.demo.Dto.BusinessCategoryDto;
import com.example.demo.Models.BusinessCategory;
import com.example.demo.Models.HashTag;
import com.example.demo.Repositories.BusinessCategoryRepository;
import com.example.demo.Repositories.HashTagRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BusinessCategoryServiceImp implements BusinessCategoryService {

    private static final Logger logger = LoggerFactory.getLogger(BusinessCategoryServiceImp.class);

    private final BusinessCategoryRepository businessCategoryRepository;

    private final HashTagRepository hashTagRepository;
    
    public BusinessCategoryServiceImp(BusinessCategoryRepository businessCategoryRepository, HashTagRepository hashTagRepository){
        this.businessCategoryRepository = businessCategoryRepository;
        this.hashTagRepository = hashTagRepository;
    }

    @Override
    public ResponseEntity<Object> getAllBusinessCategories(){
        try{
            Iterable<BusinessCategory> businessCategories = businessCategoryRepository.findAll();
            if(!businessCategories.iterator().hasNext()){
                logger.info("\nThere is Request for Fetching All Business Category, But Nothing Registed in Database");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is No  Business Category in the Database");
            }else{
                logger.info("\nSuccessful fetched all Business Category");
                return ResponseEntity.status(200).body(businessCategories);
            }
        }catch (Exception exception){
            logger.error("\nFailed to fetch all Business Category, Server Error: \n" + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server error");
        }
    }

    @Override
    public ResponseEntity<Object> saveBusinessCategory(BusinessCategoryDto businessCategoryDto){
        try {
            Optional<BusinessCategory> existingBusinessCategory = businessCategoryRepository.findByName(businessCategoryDto.getName());

            List<HashTag> hashTags = hashTagRepository.findAllByIdIn(businessCategoryDto.getHashTags());

            List<HashTag> validHashTags = hashTags.stream().filter(hashTag -> hashTag != null).collect(Collectors.toList());

        if(existingBusinessCategory.isPresent()){
            logger.error("\nFailed to save Business Category, Business Cattegory Already Exists Error");
            return ResponseEntity.status(400).body("This Business Category Already Exists!");
        }
        else{
            BusinessCategory businessCategory = new BusinessCategory();
            businessCategory.setName(businessCategoryDto.getName());
            businessCategory.setHashTags(validHashTags);
            businessCategoryRepository.save(businessCategory);
            logger.info("Business Category saved Sucessfully\n" + businessCategory);
            return ResponseEntity.status(201).body("Business Category Created Successful ");
        }
        } catch (Exception exception) {
            logger.error("\nFailed to save Business Category, Server Error: \n" + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}
