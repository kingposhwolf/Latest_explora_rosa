package com.example.demo.Components.Algorithms;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.example.demo.Models.Profile;
import com.example.demo.Models.UserPost;
import com.example.demo.Repositories.GlobalDBStartPointRepository;
import com.example.demo.Repositories.UserEngagementRepository;
import com.example.demo.Repositories.UserPostRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class FeedsAlgorithm {

    private final AlgorithmAccessories algorithmAccessories;

    private final UserPostRepository userPostRepository;

    private final UserEngagementRepository userEngagementRepository;

    private final GlobalDBStartPointRepository globalDBStartPointRepository;


    //Posts based on user Engagements and Following(Friends)
    public List<UserPost> friendPost(Profile profile){
        List<Profile> following = algorithmAccessories.fetchFollowings(profile);
        List<Profile> friends = userEngagementRepository.findTop3TopicsByTopicInAndTargetOrderByScoreDesc(following, profile);
        Pageable pageable = PageRequest.of(0, 2);

        List<UserPost> posts = userPostRepository.findByProfilesAndLikesGreaterThanStartingFromId(algorithmAccessories.searchStartPointUserEngage(profile), friends, 26000, pageable);

        if (!posts.isEmpty()) {
            UserPost lastPost = posts.get(posts.size() - 1);
            algorithmAccessories.updateUserEngagePreviousEnd(profile, lastPost.getId());
        }

        return posts;
    }

    //posts based on user interested topics.
    public List<UserPost> postBasedOnTopic(Profile profile){
        Pageable pageable = PageRequest.of(0, 2);
        List<UserPost> posts = userPostRepository.findByHashTagsInAndLikesGreaterThanAndIdGreaterThan(algorithmAccessories.fetchTopics(profile), 26000, algorithmAccessories.searchStartPointTopicEngage(profile),pageable );

        if (!posts.isEmpty()) {
            UserPost lastPost = posts.get(posts.size() - 1);
            algorithmAccessories.updateTopicEngagePreviousEnd(profile, lastPost.getId());
        }

        return posts;
    }

    //Posts based on engagements between users
    public List<UserPost> postBasedOnUserEngagements(Profile profile){
        Pageable pageable = PageRequest.of(0, 2);
        List<UserPost> posts = userPostRepository.findByProfileInAndLikesGreaterThanAndIdGreaterThan(algorithmAccessories.fetchUserEngage(profile), 26000, algorithmAccessories.searchStartPointTopicEngage(profile), pageable);

        if (!posts.isEmpty()) {
            UserPost lastPost = posts.get(posts.size() - 1);
            algorithmAccessories.updateTopicEngagePreviousEnd(profile, lastPost.getId());
        }

        return posts;
    }

    //Post based on country trendings
    public List<UserPost> locationSpecificTrendingPost(Profile profile){
        Pageable pageable = PageRequest.of(0, 2);
        List<UserPost> posts = userPostRepository.findByCountryAndLikesGreaterThanAndIdGreaterThan(profile.getCountry(),26000, globalDBStartPointRepository.findHotById((long) 1), pageable);

        return posts;
    }

    // //posts based on world wide trending
    // public List<UserPost> trendingPost(){
    //     List<UserPost> posts = userPostRepository
    // }

    // //Posts based on Following
    // public List<UserPost> followingPosts(){
    //     List<UserPost> posts = userPostRepository
    // }

    // //Posts based on Discover
    // public List<UserPost> discoverPosts(){
    //     List<UserPost> posts = userPostRepository
    // }

    // //Posts from the friend followings
    // public List<UserPost> friendsFollowingPosts(){
    //     List<UserPost> posts = userPostRepository
    // }

    // //People you may know posts
    // public List<UserPost> peopleFromMobilePosts(){
    //     List<UserPost> posts = userPostRepository
    // }
}
