package com.example.demo.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.Profile;
import com.example.demo.Models.UserEngageFeedsPreviousEnd;



public interface UserEngageFeedsPreviousEndRepository extends JpaRepository<UserEngageFeedsPreviousEnd, Long>{
    Optional<Long> findPreviousEndByUser(Profile user);

    Optional<UserEngageFeedsPreviousEnd> findByUser(Profile user);
}
