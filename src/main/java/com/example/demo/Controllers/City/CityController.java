package com.example.demo.Controllers.City;
import com.example.demo.Components.GlobalValidationFormatter.GlobalValidationFormatter;
import com.example.demo.InputDto.ManagementInfo.City.CityDto;
import com.example.demo.Services.CityService.CityServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/cities")
@AllArgsConstructor
public class CityController {

    private GlobalValidationFormatter globalValidationFormatter;

    private final CityServiceImpl cityServiceImpl;


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
