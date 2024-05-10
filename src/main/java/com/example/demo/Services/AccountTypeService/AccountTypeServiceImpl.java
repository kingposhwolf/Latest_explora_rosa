package com.example.demo.Services.AccountTypeService;

import com.example.demo.InputDto.UserManagement.AccountType.AccountTypeDto;
import com.example.demo.Models.UserManagement.Management.AccountType;
import com.example.demo.Repositories.UserManagement.AccountManagement.AccountTypeRepository;

import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountTypeServiceImpl implements AccountTypeService {

    private static final Logger logger = LoggerFactory.getLogger(AccountTypeServiceImpl.class);

    private final AccountTypeRepository accountTypeRepository;

    @Override
    public ResponseEntity<Object> getAllAccountTypes(){
        try{
            Iterable<AccountType> accountTypes =  accountTypeRepository.findAll();
            if(!accountTypes.iterator().hasNext()){
                logger.error("\nThere is Request for Fetching All Account types, But No Account Registered Yet");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is No  Account type in the Database Yet");
            }else{
                logger.info("\nSuccessful fetched all Account Types");
                return ResponseEntity.status(200).body(accountTypes);
            }
            
        }catch(Exception exception){
            logger.error("Internal server Error , During  Fetching All Account Types : " + exception.getMessage());
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

    @Transactional
    @Override
    public ResponseEntity<Object> saveAccountType(AccountTypeDto accountTypeDto){
        try {
            Optional<AccountType> existingAccountType = accountTypeRepository.findByName(accountTypeDto.getName());

            if (existingAccountType.isPresent()){
                logger.error("\nFailed to save the Account type because It Already exists");
                return ResponseEntity.status(400).body("This Account type Already Exists!");
            }
            else{
                AccountType accountType = new AccountType();
                accountType.setName(accountTypeDto.getName());
                accountTypeRepository.save(accountType);

                logger.info("\nSuccessful save Account Type" + accountType);
                return ResponseEntity.status(201).body("Account Type Created Successfully");
            }
        } catch (Exception exception) {
            logger.error("\nFailed to save the Account Type, Server Error: \n" + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}
