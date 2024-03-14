package com.example.demo.Components.Algorithms;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.demo.Models.HashTag;
import com.example.demo.Models.Profile;
import com.example.demo.Models.TopicEngageFeedPreviousEnd;
import com.example.demo.Models.UserEngageFeedsPreviousEnd;
import com.example.demo.Repositories.FollowUnFollowRepository;
import com.example.demo.Repositories.GlobalDBStartPointRepository;
import com.example.demo.Repositories.TopicEngageFeedsPreviousEndRepository;
import com.example.demo.Repositories.TopicEngagementRepository;
import com.example.demo.Repositories.UserEngageFeedsPreviousEndRepository;
import com.example.demo.Repositories.UserEngagementRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AlgorithmAccessories {

    private final UserEngagementRepository userEngagementRepository;

    private final FollowUnFollowRepository followUnFollowRepository;

    private final TopicEngagementRepository topicEngagementRepository;


    private final UserEngageFeedsPreviousEndRepository userEngageFeedsPreviousEndRepository;

    private final GlobalDBStartPointRepository globalDBStartPointRepository;

    private final TopicEngageFeedsPreviousEndRepository topicEngageFeedsPreviousEndRepository;

    //Returns topics that we think user might be interested with
    public List<HashTag> fetchTopics(Profile profile){
        List<HashTag> interestTopics = topicEngagementRepository.findTop4HashTagsByProfileOrderByScoreDesc(profile);
        return interestTopics;
    }

    //Returns Specific profiles that user might be interested with
    public List<Profile> fetchUserEngage(Profile profile){
        List<Profile> interestProfile = userEngagementRepository.findTop4TopicByTargetOrderByScoreDesc(profile);
        return interestProfile;
    }

    //Returns user followings
    public List<Profile> fetchFollowings(Profile profile){
        List<Profile> following = followUnFollowRepository.findFollowingByFollower(profile);
        return following;
    }

    //Returns Id in the Database where we start our search, when dealing with User Engagement
    public Long searchStartPointUserEngage(Profile profile){
        Optional<Long> previousEnd = userEngageFeedsPreviousEndRepository.findPreviousEndByUser(profile);
        Long globalFeedPoint = globalDBStartPointRepository.findHotById((long) 1);
        if(previousEnd.isPresent()){
            if(previousEnd.get() >= globalFeedPoint){
                return previousEnd.get();
            }else{
                return globalFeedPoint;
            }
        }else{
            return globalFeedPoint;
        }
    }

    //Returns Id in the Database where we start our search, when dealing with TOpic Engagement
    public Long searchStartPointTopicEngage(Profile profile){
        Optional<Long> previousEnd = topicEngageFeedsPreviousEndRepository.findPreviousEndByUser(profile);
        Long globalFeedPoint = globalDBStartPointRepository.findHotById((long) 1);
        if(previousEnd.isPresent()){
            if(previousEnd.get() >= globalFeedPoint){
                return previousEnd.get();
            }else{
                return globalFeedPoint;
            }
        }else{
            return globalFeedPoint;
        }
    }

    //Update the point where our search will begin when  dealing with User Engagement
    @Transactional
    public void updateUserEngagePreviousEnd(Profile profile, Long endPoint){
        Optional<UserEngageFeedsPreviousEnd> previousEnd = userEngageFeedsPreviousEndRepository.findByUser(profile);
        if(previousEnd.isPresent()){
            UserEngageFeedsPreviousEnd previousEnd2 = previousEnd.get();
            if(previousEnd2.getPreviousEnd() < endPoint){
                previousEnd2.setPreviousEnd(endPoint);
                userEngageFeedsPreviousEndRepository.save(previousEnd2);
            }
        }else{
            UserEngageFeedsPreviousEnd previousEnd2 = new UserEngageFeedsPreviousEnd();
            previousEnd2.setUser(profile);
            previousEnd2.setPreviousEnd(endPoint);
            userEngageFeedsPreviousEndRepository.save(previousEnd2);
        }
        
    }

    //Update the point where our search will begin when  dealing with Topic Engagement
    @Transactional
    public void updateTopicEngagePreviousEnd(Profile profile, Long endPoint){
        Optional<TopicEngageFeedPreviousEnd> previousEnd = topicEngageFeedsPreviousEndRepository.findByUser(profile);
        if(previousEnd.isPresent()){
            TopicEngageFeedPreviousEnd previousEnd2 = previousEnd.get();
            if(previousEnd2.getPreviousEnd() < endPoint){
                previousEnd2.setPreviousEnd(endPoint);
                topicEngageFeedsPreviousEndRepository.save(previousEnd2);
            }
        }else{
            TopicEngageFeedPreviousEnd previousEnd2 = new TopicEngageFeedPreviousEnd();
            previousEnd2.setUser(profile);
            previousEnd2.setPreviousEnd(endPoint);
            topicEngageFeedsPreviousEndRepository.save(previousEnd2);
        }
        
    }
}
