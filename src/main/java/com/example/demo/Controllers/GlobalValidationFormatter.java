package com.example.demo.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
public class GlobalValidationFormatter {
    public ResponseEntity<String> validationFormatter(BindingResult bindingResult){
        List<String> errors = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getField() + ": " + error.getDefaultMessage());
            }

            return ResponseEntity.badRequest().body("Validation errors: " + String.join(", ", errors));
    }
}
