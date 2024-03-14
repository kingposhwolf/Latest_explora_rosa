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

//Returns posts based on Hashtags and higher likes
List<UserPost> findByHashTagsInAndLikesGreaterThanAndIdGreaterThan(List<HashTag> hashTags, int likesThreshold, Long startId, Pageable pageable);

//Returns post based on given profiles and higher likes
List<UserPost> findByProfileInAndLikesGreaterThanAndIdGreaterThan(List<Profile> profiles, int likesThreshold, Long startId, Pageable pageable);

//Return the tranding post for a given Country
List<UserPost> findByCountryAndLikesGreaterThanAndIdGreaterThan(Country country, int likesThreshold, Long startId, Pageable pageable);

//Used in search Algorithm to search the given keyword on the HashTag, with bigger likes
@Query("SELECT DISTINCT up FROM UserPost up " +"JOIN up.hashTags ht " +"WHERE LOWER(ht.name) LIKE LOWER(concat('%', :keyword, '%')) " +
"AND up.likes = (SELECT MAX(up2.likes) FROM UserPost up2 " + "JOIN up2.hashTags ht2 " +
"WHERE LOWER(ht2.name) LIKE LOWER(concat('%', :keyword, '%')))")
List<UserPost> findByHashTagsContainingKeywordAndMaxLikes( @Param("keyword") String keyword, Pageable pageable);


@Query("SELECT DISTINCT up FROM UserPost up " + "JOIN up.hashTags ht " + "WHERE ht.name = :keyword " +
"AND up.likes = (SELECT MAX(up2.likes) FROM UserPost up2 " + "JOIN up2.hashTags ht2 WHERE ht2.name = :keyword)")
List<UserPost> findByHashTagsMatchKeywordAndMaxLikes(@Param("keyword") String keyword,Pageable pageable);

}
