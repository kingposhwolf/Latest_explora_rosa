package com.example.demo.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dto.TitleDto;
import com.example.demo.Services.TitleService.TitleServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/title")
public class TitleController {
    private final TitleServiceImpl titleServiceImpl;

    public TitleController(TitleServiceImpl titleServiceImpl) {
        this.titleServiceImpl = titleServiceImpl;
    }

     @GetMapping("/all")
    public ResponseEntity<Object> getAllTitles() {
        return titleServiceImpl.getAllTitle();
    }
    
    @PostMapping("/register")
    public ResponseEntity<Object> registerTitle(@RequestBody @Valid TitleDto titleDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getField() + ": " + error.getDefaultMessage());
            }

            return ResponseEntity.badRequest().body("Validation errors: " + String.join(", ", errors));
        }
        return titleServiceImpl.saveTitle(titleDto);
    }
}
