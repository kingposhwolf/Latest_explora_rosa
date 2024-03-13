package com.example.demo.Repositories;

import com.example.demo.Models.Country;
import com.example.demo.Models.HashTag;
import com.example.demo.Models.Profile;
import com.example.demo.Models.UserPost;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPostRepository extends JpaRepository<UserPost, Long> {
    @Query("SELECT up FROM UserPost up WHERE up.id >= :startId AND :hashTag MEMBER OF up.hashTags AND up.likes > :likesThreshold")
    List<UserPost> findByHashTagsAndLikesGreaterThanStartingFromId(
            @Param("startId") Long startId,
            @Param("hashTag") HashTag hashTag,
            @Param("likesThreshold") int likesThreshold,
            Pageable pageable
    );

    @Query("SELECT up FROM UserPost up WHERE up.id >= :startId AND up.profile IN :profiles AND up.likes > :likesThreshold")
List<UserPost> findByProfilesAndLikesGreaterThanStartingFromId(
        @Param("startId") Long startId,
        @Param("profiles") List<Profile> profiles,
        @Param("likesThreshold") int likesThreshold,
        Pageable pageable
);

List<UserPost> findByHashTagsInAndLikesGreaterThanAndIdGreaterThan(List<HashTag> hashTags, int likesThreshold, Long startId, Pageable pageable);

List<UserPost> findByProfileInAndLikesGreaterThanAndIdGreaterThan(List<Profile> profiles, int likesThreshold, Long startId, Pageable pageable);

List<UserPost> findByCountryAndLikesGreaterThanAndIdGreaterThan(Country country, int likesThreshold, Long startId, Pageable pageable);
}
