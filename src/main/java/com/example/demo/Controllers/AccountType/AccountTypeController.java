package com.example.demo.Controllers.AccountType;
/*
 * @author Dwight Danda
 *
 */

import com.example.demo.Components.GlobalValidationFormatter.GlobalValidationFormatter;
import com.example.demo.InputDto.UserManagement.AccountType.AccountTypeDto;
import com.example.demo.Services.AccountTypeService.AccountTypeServiceImpl;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accountType")
@AllArgsConstructor
public class AccountTypeController {

    private GlobalValidationFormatter globalValidationFormatter;

    private final AccountTypeServiceImpl accountTypeServiceImpl;

    @PostMapping("/all")
    public ResponseEntity<Object> getAllAccountTypes() {
        return accountTypeServiceImpl.getAllAccountTypes();
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerAccountTypes(@RequestBody @Valid AccountTypeDto accountTypeDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return accountTypeServiceImpl.saveAccountType(accountTypeDto);
    }
}
