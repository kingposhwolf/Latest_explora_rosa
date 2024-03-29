package com.example.demo.Repositories;
/*
 * @author Dwight Danda
 *
 */
import org.springframework.data.repository.CrudRepository;

import com.example.demo.Models.UserManagement.Management.AccountType;

import java.util.Optional;

public interface AccountTypeRepository  extends CrudRepository<AccountType, Long> {
    Optional<AccountType> findByName(String name);

}
