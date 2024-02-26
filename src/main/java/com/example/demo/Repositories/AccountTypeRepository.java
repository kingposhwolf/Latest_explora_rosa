package com.example.demo.Repositories;
/*
 * @author Dwight Danda
 *
 */
import com.example.demo.Models.AccountType;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountTypeRepository  extends CrudRepository<AccountType, Long> {
    Optional<AccountType> findByName(String name);

}
