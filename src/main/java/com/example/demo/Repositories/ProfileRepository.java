package com.example.demo.Repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.Models.UserManagement.Profile;
import com.example.demo.Models.UserManagement.User;




public interface ProfileRepository extends CrudRepository<Profile, Long>{
    Profile findByUser(User user);
}
