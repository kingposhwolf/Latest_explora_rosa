package com.example.demo.Repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.Models.UserManagement.PersonalAccount.Personal;


public interface PersonalRepository extends CrudRepository<Personal , Long>{
    
}
