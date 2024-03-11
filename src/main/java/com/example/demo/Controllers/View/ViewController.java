package com.example.demo.Controllers.View;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Components.GlobalValidationFormatter.GlobalValidationFormatter;
import com.example.demo.Dto.ViewDto;
import com.example.demo.Services.ViewService.ViewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/view")
@RequiredArgsConstructor
public class ViewController {
    private final ViewService viewService;

    private GlobalValidationFormatter globalValidationFormatter;

    @PostMapping("/add")
    public ResponseEntity<Object> newComment(@RequestBody @Valid ViewDto viewDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return viewService.viewOperation(viewDto);
    }
}
