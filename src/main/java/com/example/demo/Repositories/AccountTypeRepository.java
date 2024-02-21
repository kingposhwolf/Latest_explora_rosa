package com.example.demo.Repositories;

import com.example.demo.Models.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountTypeRepository  extends JpaRepository<AccountType, Long> {
    Optional<AccountType> findByName(String name);

}
