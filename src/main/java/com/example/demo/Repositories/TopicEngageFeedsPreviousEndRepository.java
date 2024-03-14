package com.example.demo.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.Profile;
import com.example.demo.Models.TopicEngageFeedPreviousEnd;

public interface TopicEngageFeedsPreviousEndRepository extends JpaRepository<TopicEngageFeedPreviousEnd, Long>{
    Optional<Long> findPreviousEndByUser(Profile user);

    Optional<TopicEngageFeedPreviousEnd> findByUser(Profile user);
}
