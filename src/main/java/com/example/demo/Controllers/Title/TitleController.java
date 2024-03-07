package com.example.demo.Controllers.Title;

import com.example.demo.Controllers.GlobalValidationFormatter.GlobalValidationFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
    @Autowired
    private GlobalValidationFormatter globalValidationFormatter;

    private final TitleServiceImpl titleServiceImpl;

    public TitleController(TitleServiceImpl titleServiceImpl) {
        this.titleServiceImpl = titleServiceImpl;
    }

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
