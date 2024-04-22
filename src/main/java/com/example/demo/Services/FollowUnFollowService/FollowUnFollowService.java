package com.example.demo.Services.FollowUnFollowService;

import org.springframework.http.ResponseEntity;

import com.example.demo.InputDto.FollowUnFollowDto;

public interface FollowUnFollowService {
    ResponseEntity<Object> followOperation(FollowUnFollowDto followUnFollowDto);

    ResponseEntity<Object> fetchFollowers(Long profileId);

    ResponseEntity<Object> fetchFollowing(Long profileId);
}
