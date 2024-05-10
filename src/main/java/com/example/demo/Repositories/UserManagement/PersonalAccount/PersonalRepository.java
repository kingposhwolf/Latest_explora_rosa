package com.example.demo.Repositories.UserManagement.PersonalAccount;

import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Models.UserManagement.PersonalAccount.Personal;


public interface PersonalRepository extends JpaRepository<Personal , Long>{
    
    @Query("SELECT DISTINCT l.id as id, l.user.username as username, l.user.name as name, l.user.email as email, l.user.accountType.name as accountTypeName, l.user.accountType.id as accountTypeId, l.profilePicture as profilePicture, l.coverPhoto as coverPhoto, l.bio as bio, l.address as address, c as country, l.verificationStatus as verificationStatus, l.city.id as cityId, l.followers as followers, l.following as following, l.posts as posts, l.powerSize as powerSize, d as title FROM Personal l LEFT JOIN l.country c LEFT JOIN l.title d WHERE  l.id = :id")
    Map<String, Object> findProfileInfoById(@Param("id") Long id);

}
