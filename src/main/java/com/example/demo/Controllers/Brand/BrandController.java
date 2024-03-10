package com.example.demo.Controllers.Brand;

import com.example.demo.Controllers.GlobalValidationFormatter.GlobalValidationFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dto.ProfileVisitDto;
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

    @PostMapping("/visit")
    public ResponseEntity<Object> brandById(@RequestBody @Valid ProfileVisitDto brandVisitDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return brandServiceImpl.getBrandById(brandVisitDto);
    }
}
