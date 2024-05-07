package com.example.demo.Components.Algorithms;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

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
    public List<Map<String, Object>> suggestiveProfiles(Long profileId, String keyword){

        List<Map<String, Object>> interact = userEngagementRepository.searchByTargetAndTopic(profileId, keyword);

        List<Map<String, Object>> followings = followUnFollowRepository.searchOnFollowing(profileId, keyword);

        interact.addAll(followings);

        HashSet<Object> seen = new HashSet<>();

        interact.removeIf(e -> !seen.add(e.get("profileId")));
        

        return interact;
    }

    //Search on Location
    public List<Map<String, Object>> searchOnFollowings(Long profileId, String keyword){
        return profileRepository.searchByUserFollowings(profileId, keyword);
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
