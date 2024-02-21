package com.example.demo.Services.AccountTypeService;

import com.example.demo.Dto.AccountTypeDto;
import com.example.demo.Models.AccountType;
import org.springframework.stereotype.Service;


public interface AccountTypeService {
    Iterable<AccountType> getAllAccountTypes();

    AccountType saveAccountType(AccountTypeDto accountTypeDto);
}
