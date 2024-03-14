package com.example.demo.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.FollowUnFollow;
import com.example.demo.Models.Profile;

public interface FollowUnFollowRepository extends JpaRepository<FollowUnFollow, Long>{
    List<Profile> findFollowerByFollowing(Profile profile);
    List<Profile> findFollowingByFollower(Profile profile);
}
