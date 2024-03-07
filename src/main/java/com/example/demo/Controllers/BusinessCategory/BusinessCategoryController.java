package com.example.demo.Controllers.BusinessCategory;

import com.example.demo.Controllers.GlobalValidationFormatter.GlobalValidationFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dto.BusinessCategoryDto;
import com.example.demo.Services.BusinessCategoryService.BusinessCategoryServiceImp;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/business-categories")
public class BusinessCategoryController {
    @Autowired
    private GlobalValidationFormatter globalValidationFormatter;

    private final BusinessCategoryServiceImp businessCategoryServiceImp;

    public BusinessCategoryController(BusinessCategoryServiceImp businessCategoryServiceImp) {
        this.businessCategoryServiceImp = businessCategoryServiceImp;
    }
    
    @PostMapping("/all")
    public ResponseEntity<Object> getAllBusinessCategories() {
        return businessCategoryServiceImp.getAllBusinessCategories();
    }
    
    @PostMapping("/register")
    public ResponseEntity<Object> registerBusinessCategory(@RequestBody @Valid BusinessCategoryDto businessCategoryDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return businessCategoryServiceImp.saveBusinessCategory(businessCategoryDto);
    }
}