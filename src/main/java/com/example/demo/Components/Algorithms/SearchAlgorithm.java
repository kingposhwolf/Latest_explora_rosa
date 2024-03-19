package com.example.demo.Components.Algorithms;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.example.demo.Models.UserPost;
import com.example.demo.Repositories.UserPostRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class SearchAlgorithm {

    private final UserPostRepository userPostRepository;

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
