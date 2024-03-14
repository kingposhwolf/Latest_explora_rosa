package com.example.demo.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.TopicEngagement;
import com.example.demo.Models.HashTag;
import com.example.demo.Models.Profile;


public interface TopicEngagementRepository extends JpaRepository<TopicEngagement, Long>{
    Optional<TopicEngagement> findByProfileAndHashTags(Profile profile, HashTag hashTags);

    List<HashTag> findTop4HashTagsByProfileOrderByScoreDesc(Profile profile);
}
