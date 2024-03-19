package com.example.demo.Components.Algorithms;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ReelsAlgorithm {
    

    // //Reels based on user Engagements and Following(Friends)
    // public List<UserPost> friendReels(Profile profile){
    //     List<Profile> following = algorithmAccessories.fetchFollowings(profile);
    //     List<Profile> friends = userEngagementRepository.findTop3TopicsByTopicInAndTargetOrderByScoreDesc(following, profile);
    //     Pageable pageable = PageRequest.of(0, 2);

    //     List<UserPost> posts = userPostRepository.findByProfilesAndLikesGreaterThanStartingFromId(algorithmAccessories.searchStartPointUserEngage(profile), friends, 26000, pageable);

    //     if (!posts.isEmpty()) {
    //         UserPost lastPost = posts.get(posts.size() - 1);
    //         algorithmAccessories.updateUserEngagePreviousEnd(profile, lastPost.getId());
    //     }

    //     return posts;
    // }

    // //reels based on user interested topics.
    // public List<UserPost> reelsBasedOnTopic(Profile profile){
    //     Pageable pageable = PageRequest.of(0, 2);
    //     List<UserPost> posts = userPostRepository.findByHashTagsInAndLikesGreaterThanAndIdGreaterThan(algorithmAccessories.fetchTopics(profile), 26000, algorithmAccessories.searchStartPointTopicEngage(profile),pageable );

    //     if (!posts.isEmpty()) {
    //         UserPost lastPost = posts.get(posts.size() - 1);
    //         algorithmAccessories.updateTopicEngagePreviousEnd(profile, lastPost.getId());
    //     }

    //     return posts;
    // }

    // //Reels based on engagements between users
    // public List<UserPost> reelsBasedOnUserEngagements(Profile profile){
    //     Pageable pageable = PageRequest.of(0, 2);
    //     List<UserPost> posts = userPostRepository.findByProfileInAndLikesGreaterThanAndIdGreaterThan(algorithmAccessories.fetchUserEngage(profile), 26000, algorithmAccessories.searchStartPointTopicEngage(profile), pageable);

    //     if (!posts.isEmpty()) {
    //         UserPost lastPost = posts.get(posts.size() - 1);
    //         algorithmAccessories.updateTopicEngagePreviousEnd(profile, lastPost.getId());
    //     }

    //     return posts;
    // }

    // //Reels based on country trendings
    // public List<UserPost> locationSpecificTrendingReels(Profile profile){
    //     Pageable pageable = PageRequest.of(0, 2);
    //     List<UserPost> posts = userPostRepository.findByCountryAndLikesGreaterThanAndIdGreaterThan(profile.getCountry(),26000, globalDBStartPointRepository.findHotById((long) 1), pageable);

    //     return posts;
    // }

    // //Reels based on world wide trending
    // public List<UserPost> trendingReels(){
    //     List<UserPost> posts = userPostRepository
    // }

    // //Posts based on Following
    // public List<UserPost> followingReels(){
    //     List<UserPost> posts = userPostRepository
    // }

    // //Reels based on Discover
    // public List<UserPost> discoverReels(){
    //     List<UserPost> posts = userPostRepository
    // }

    // //Reels from the friend followings
    // public List<UserPost> friendsFollowingReels(){
    //     List<UserPost> posts = userPostRepository
    // }

    // //People you may know reels
    // public List<UserPost> peopleFromMobileReels(){
    //     List<UserPost> posts = userPostRepository
    // }
}
