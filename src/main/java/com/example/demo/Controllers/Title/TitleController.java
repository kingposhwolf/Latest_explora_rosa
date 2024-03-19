package com.example.demo.Controllers.Title;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Components.GlobalValidationFormatter.GlobalValidationFormatter;
import com.example.demo.InputDto.TitleDto;
import com.example.demo.Services.TitleService.TitleServiceImpl;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/title")
@AllArgsConstructor
public class TitleController {
  
    private GlobalValidationFormatter globalValidationFormatter;

    private final TitleServiceImpl titleServiceImpl;


     @PostMapping("/all")
    public ResponseEntity<Object> getAllTitles() {
        return titleServiceImpl.getAllTitle();
    }
    
    @PostMapping("/register")
    public ResponseEntity<Object> registerTitle(@RequestBody @Valid TitleDto titleDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return titleServiceImpl.saveTitle(titleDto);
    }
}
