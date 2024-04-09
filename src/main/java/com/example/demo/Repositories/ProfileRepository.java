package com.example.demo.Repositories;

import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.Models.UserManagement.Profile;
import com.example.demo.Models.UserManagement.User;




public interface ProfileRepository extends CrudRepository<Profile, Long>{
    Profile findByUser(User user);

    @Query("SELECT DISTINCT l.id as id FROM Profile l WHERE  l.id = :profileId")
Map<String, Object> findProfileIdById(@Param("profileId") Long profileId);

}
