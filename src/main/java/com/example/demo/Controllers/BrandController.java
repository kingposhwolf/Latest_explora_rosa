package com.example.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dto.BrandDto;
import com.example.demo.Services.BrandService.BrandServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/brand")
public class BrandController {
    @Autowired
    private GlobalValidationFormatter globalValidationFormatter;

    private final BrandServiceImpl brandServiceImpl;

    public BrandController(BrandServiceImpl brandServiceImpl){
        this.brandServiceImpl = brandServiceImpl;
    }

    @PostMapping("/by-id")
    public ResponseEntity<Object> brandById(@RequestBody @Valid BrandDto brandDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return brandServiceImpl.getBrandById(brandDto);
    }
}
