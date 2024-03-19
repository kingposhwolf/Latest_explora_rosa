package com.example.demo.Repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.Models.Profile;
import com.example.demo.Models.User;



public interface ProfileRepository extends CrudRepository<Profile, Long>{
    Profile findByUser(User user);
}
