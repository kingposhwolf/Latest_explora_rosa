package com.example.demo.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.Models.UserManagement.Profile;
import com.example.demo.Models.UserManagement.User;
import java.util.List;
import com.example.demo.Models.UserManagement.Management.Status;





public interface ProfileRepository extends CrudRepository<Profile, Long>{
    Profile findByUser(User user);

    @Query("SELECT DISTINCT l.id FROM Profile l WHERE  l.id = :profileId")
    Long findProfileIdById(@Param("profileId") Long profileId);

    @Query("SELECT DISTINCT l FROM Profile l WHERE  l.id = :profileId")
    Optional<Profile> findProfilesById(@Param("profileId") Long profileId);

    Optional<List<Profile>> findByStatus(Status status);
}
