package com.example.demo.Controllers;


import com.example.demo.Dto.AccountTypeDto;
import com.example.demo.Models.AccountType;
import com.example.demo.Services.AccountTypeService.AccountTypeServiceImpl;
import com.example.demo.Services.CountryService.DuplicateCountryException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accountType")
public class AccountTypeController {

    private final AccountTypeServiceImpl accountTypeServiceImpl;

    public AccountTypeController(AccountTypeServiceImpl accountTypeServiceImpl) {
        this.accountTypeServiceImpl = accountTypeServiceImpl;
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllAccountTypes() {
        Iterable<AccountType> accountTypes = accountTypeServiceImpl.getAllAccountTypes();

        if (!accountTypes.iterator().hasNext()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no registered account type yet");
        }
        else{
            return ResponseEntity.ok().body(accountTypes);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerAccountTypes(@RequestBody AccountTypeDto accountTypeDto) {
        try {
            AccountType savedAccountType = accountTypeServiceImpl.saveAccountType(accountTypeDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAccountType);
        } catch (DuplicateCountryException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account Type with the same name already exists");
        }
    }
}
