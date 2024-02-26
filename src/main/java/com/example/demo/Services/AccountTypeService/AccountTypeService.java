package com.example.demo.Services.AccountTypeService;

import org.springframework.http.ResponseEntity;

import com.example.demo.Dto.AccountTypeDto;


public interface AccountTypeService {
    ResponseEntity<Object> getAllAccountTypes();

    ResponseEntity<Object> saveAccountType(AccountTypeDto accountTypeDto);
}
