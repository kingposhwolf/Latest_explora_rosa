package com.example.demo.Repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.Models.Profile;
import com.example.demo.Models.UserEngagement;

public interface UserEngagementRepository extends CrudRepository<UserEngagement, Long>{
    Optional<UserEngagement> findByTargetAndTopic(Profile target, Profile topic);
}
