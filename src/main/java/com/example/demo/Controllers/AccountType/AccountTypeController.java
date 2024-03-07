package com.example.demo.Controllers.AccountType;
/*
 * @author Dwight Danda
 *
 */

import com.example.demo.Controllers.GlobalValidationFormatter.GlobalValidationFormatter;
import com.example.demo.Dto.AccountTypeDto;
import com.example.demo.Services.AccountTypeService.AccountTypeServiceImpl;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accountType")
public class AccountTypeController {

    @Autowired
    private GlobalValidationFormatter globalValidationFormatter;

    private final AccountTypeServiceImpl accountTypeServiceImpl;

    public AccountTypeController(AccountTypeServiceImpl accountTypeServiceImpl) {
        this.accountTypeServiceImpl = accountTypeServiceImpl;
    }

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
