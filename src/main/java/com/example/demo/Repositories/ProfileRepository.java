package com.example.demo.Repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.Models.Profile;

public interface ProfileRepository extends CrudRepository<Profile, Long>{
    
}
