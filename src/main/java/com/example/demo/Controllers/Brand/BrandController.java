package com.example.demo.Controllers.Brand;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Components.GlobalValidationFormatter.GlobalValidationFormatter;
import com.example.demo.InputDto.ProfileVisitDto;
import com.example.demo.Services.BrandService.BrandServiceImpl;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/brand")
@AllArgsConstructor
public class BrandController {
    
    private GlobalValidationFormatter globalValidationFormatter;

    private final BrandServiceImpl brandServiceImpl;


    @PostMapping("/visit")
    public ResponseEntity<Object> brandById(@RequestBody @Valid ProfileVisitDto brandVisitDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return brandServiceImpl.getBrandById(brandVisitDto);
    }
}
