package com.example.demo.Services.AccountTypeService;

import com.example.demo.Dto.AccountTypeDto;
import com.example.demo.Models.AccountType;
import com.example.demo.Repositories.AccountTypeRepository;
import com.example.demo.Services.CountryService.DuplicateCountryException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountTypeServiceImpl implements AccountTypeService {

    private final AccountTypeRepository accountTypeRepository;

    public AccountTypeServiceImpl(AccountTypeRepository accountTypeRepository){
        this.accountTypeRepository = accountTypeRepository;
    }

    public Iterable<AccountType> getAllAccountTypes(){
     return accountTypeRepository.findAll();
    }

    public AccountType saveAccountType(AccountTypeDto accountTypeDto){
        Optional<AccountType> existingCountry = accountTypeRepository.findByName(accountTypeDto.getName());

        if (existingCountry.isPresent()) {

            throw new DuplicateCountryException("Account type already existed");
        }
        AccountType accountType = new AccountType();
        accountType.setName(accountTypeDto.getName());
        return accountTypeRepository.save(accountType);
    }
}
