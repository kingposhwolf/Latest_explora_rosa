package com.example.demo.Components.Algorithms;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.example.demo.Components.Helper.Helper;
import com.example.demo.InputDto.SearchDto.SearchDto;
import com.example.demo.Models.SocialMedia.UserPost;
import com.example.demo.Repositories.FollowUnFollowRepository;
import com.example.demo.Repositories.ProfileRepository;
import com.example.demo.Repositories.UserEngagementRepository;
import com.example.demo.Repositories.UserPostRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class SearchAlgorithm {

    private final UserPostRepository userPostRepository;

    private final UserEngagementRepository userEngagementRepository;

    private final FollowUnFollowRepository followUnFollowRepository;

    private final ProfileRepository profileRepository;

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

    //Search on Location
    public List<Map<String, Object>> suggestiveProfiles(SearchDto searchDto){

        List<Map<String, Object>> interact = userEngagementRepository.searchByTargetAndTopic(searchDto.getProfileId(), searchDto.getKeyword());

        List<Map<String, Object>> followings = followUnFollowRepository.searchOnFollowing(searchDto.getProfileId(), searchDto.getKeyword());

        List<Map<String, Object>> followingfollowings = helper.mergeProfiles(profileRepository.searchByUserFollowings(searchDto.getProfileId(), searchDto.getKeyword()));

        List<Map<String, Object>> countryFames = profileRepository.searchOnCountryFame(searchDto.getCountryId(), searchDto.getKeyword());

        followingfollowings.addAll(interact);
        followingfollowings.addAll(followings);
        followingfollowings.addAll(countryFames);

        HashSet<Object> seen = new HashSet<>();

        followingfollowings.removeIf(e -> {
            Object profileId = e.get("profileId");
            return !seen.add(profileId) || profileId.equals(searchDto.getProfileId());
        });
        

        return followingfollowings;
    }

    //Search on Location
    public List<Map<String, Object>> searchOnCountryFame(Long countryId, String keyword){
        return profileRepository.searchOnCountryFame(countryId, keyword);
    }

    // //Search on tags (Here we searched for the account that is tagged on the post)
    // public List<UserPost> searchOnTags(Profile profile, String keyword){

    //     Pageable pageable = PageRequest.of(0, 2);
    //     List<UserPost> posts = userPostRepository.

    //     return posts;
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
