package com.example.demo.Repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.Models.UserManagement.PersonalAccount.Title;


public interface TitleRepository extends CrudRepository<Title, Long>{
    Optional<Title> findByName(String name);
}
