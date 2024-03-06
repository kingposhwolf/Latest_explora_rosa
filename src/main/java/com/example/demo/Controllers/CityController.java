package com.example.demo.Controllers;
/*
 * @author Dwight Danda
 *
 */
import com.example.demo.Dto.CityDto;
import com.example.demo.Services.CityService.CityServiceImpl;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/cities")
public class CityController {
    @Autowired
    private GlobalValidationFormatter globalValidationFormatter;

    private final CityServiceImpl cityServiceImpl;

    public CityController(CityServiceImpl cityServiceImpl){
        this.cityServiceImpl = cityServiceImpl;
    }

    @PostMapping("/all")
    public ResponseEntity<Object> getAllCities(){
       return cityServiceImpl.getAllCities();
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerCity(@RequestBody @Valid CityDto cityDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return cityServiceImpl.saveCity(cityDto);
    }
}
