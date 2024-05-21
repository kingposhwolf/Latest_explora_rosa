package com.example.demo.Components.Algorithms;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.example.demo.Components.Helper.Helper;
import com.example.demo.InputDto.SearchDto.SearchDto;
import com.example.demo.Models.SocialMedia.UserPost;
import com.example.demo.Repositories.SocialMedia.Content.UserPostRepository;
import com.example.demo.Repositories.SocialMedia.FollowUnFollow.FollowUnFollowRepository;
import com.example.demo.Repositories.Tracking.UserEngagementRepository;
import com.example.demo.Repositories.UserManagement.AccountManagement.ProfileRepository;
import com.example.demo.Services.RedisService.RedisService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class SearchAlgorithm {

    private final UserPostRepository userPostRepository;

    private final UserEngagementRepository userEngagementRepository;

    private final FollowUnFollowRepository followUnFollowRepository;

    private final ProfileRepository profileRepository;

    private final RedisService redisService;

    private final Helper helper;

    //Search on keyword if contains in Topic
    public List<UserPost> topicContainKeyword(String keyword){

        Pageable pageable = PageRequest.of(0, 10);
        List<UserPost> posts = userPostRepository.findByHashTagsContainingKeywordAndMaxLikes(keyword, pageable);

        return posts;
    }

    //Search on keyword exactly Match Topic
    public List<UserPost> topicExactlyMatchKeyword(String keyword){

        Pageable pageable = PageRequest.of(0, 10);
        List<UserPost> posts = userPostRepository.findByHashTagsMatchKeywordAndMaxLikes(keyword, pageable);

        return posts;
    }

    //Search for suggestive profiles
    public List<Map<String, Object>> suggestiveProfiles(SearchDto searchDto){

        //Must add search for the world wide star match
        List<Map<String, Object>> followings = followUnFollowRepository.searchOnFollowing(searchDto.getProfileId(), searchDto.getKeyword());

        List<Map<String, Object>> followingfollowings = helper.mergeProfiles(profileRepository.searchByUserFollowings(searchDto.getProfileId(), searchDto.getKeyword()));
        List<Map<String, Object>> fames = profileRepository.searchOnFame(searchDto.getKeyword());

        List<Map<String, Object>> interact = userEngagementRepository.searchByTargetAndTopic(searchDto.getProfileId(), searchDto.getKeyword());

        // List<Map<String, Object>> countryFames = profileRepository.searchOnCountryFame(searchDto.getCountryId(), searchDto.getKeyword());


        followings.addAll(followingfollowings);
        followings.addAll(fames);
        followings.addAll(interact);
        // followingfollowings.addAll(anyone);

        HashSet<Long> seen = new HashSet<>();
        List<Long> uniqueProfileIds = new ArrayList<>();

        followings.removeIf(e -> {
            Long profileId = (Long) e.get("profileId");
            if (!seen.contains(profileId) && !profileId.equals(searchDto.getProfileId())) {
                seen.add(profileId);
                uniqueProfileIds.add(profileId);
                return false; // Keep the entry
            } else {
                return true; // Remove the entry
            }
            });

            // Using Java Streams to retrieve all values associated with the key "id" and store them in a List<Long>
            redisService.saveDataWithDynamicExpiration(searchDto.getProfileId().toString(),uniqueProfileIds,Duration.ofMinutes(5));

        return followings;
    }


    //Search results profile
    public List<Map<String, Object>> resultsProfiles(SearchDto searchDto){

        //Must add search for the world wide star match
        List<Map<String, Object>> followings = followUnFollowRepository.searchOnFollowing(searchDto.getProfileId(), searchDto.getKeyword());

        List<Map<String, Object>> followingfollowings = helper.mergeProfiles(profileRepository.searchByUserFollowings(searchDto.getProfileId(), searchDto.getKeyword()));
        List<Map<String, Object>> fames = profileRepository.searchOnFame(searchDto.getKeyword());

        List<Map<String, Object>> interact = userEngagementRepository.searchByTargetAndTopic(searchDto.getProfileId(), searchDto.getKeyword());

        // List<Map<String, Object>> countryFames = profileRepository.searchOnCountryFame(searchDto.getCountryId(), searchDto.getKeyword());

        List<Map<String, Object>> anyone = profileRepository.searchForAnyMatch(searchDto.getKeyword());


        
        
        followings.addAll(followingfollowings);
        followings.addAll(fames);
        followings.addAll(interact);
        followings.addAll(anyone);

        HashSet<Object> seen = new HashSet<>();

        followings.removeIf(e -> {
            Object profileId = e.get("profileId");
            return !seen.add(profileId) || profileId.equals(searchDto.getProfileId());
        });
        

        return followings;
    }

    //Search on tags (Here we searched for the account that is tagged on the post)
    // public List<Map<String, Object>> searchForAnyAccountMatch(SearchDto searchDto){
    //     List<Map<String, Object>> anyMatch = profileRepository.searchForAnyMatch(searchDto.getKeyword());
    // }

    // //Search on username (Returns Accounts)
    // public List<UserPost> searchOnUsername(Profile profile, String keyword){

    //     Pageable pageable = PageRequest.of(0, 2);
    //     List<UserPost> posts = userPostRepository.

    //     return posts;
    // }

    // //Search on name (Returns Accounts)
    // public List<UserPost> searchOnUsername(Profile profile, String keyword){

    //     Pageable pageable = PageRequest.of(0, 2);
    //     List<UserPost> posts = userPostRepository.

    //     return posts;
    // }

    // //Search on Location
    // public List<UserPost> searchOnTopics(Profile profile, String keyword){

    //     Pageable pageable = PageRequest.of(0, 2);
    //     List<UserPost> posts = userPostRepository.

    //     return posts;
    // }
}
