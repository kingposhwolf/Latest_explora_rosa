package com.example.demo.Controllers.BusinessCategory;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Components.GlobalValidationFormatter.GlobalValidationFormatter;
import com.example.demo.Dto.BusinessCategoryDto;
import com.example.demo.Services.BusinessCategoryService.BusinessCategoryServiceImp;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/business-categories")
@AllArgsConstructor
public class BusinessCategoryController {

    private GlobalValidationFormatter globalValidationFormatter;

    private final BusinessCategoryServiceImp businessCategoryServiceImp;
    
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
