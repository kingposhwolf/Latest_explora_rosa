package com.example.demo.Repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.Models.TopicEngagement;
import com.example.demo.Models.HashTag;
import com.example.demo.Models.Profile;


public interface TopicEngagementRepository extends CrudRepository<TopicEngagement, Long>{
    Optional<TopicEngagement> findByProfileAndHashTags(Profile profile, HashTag hashTags);
}
