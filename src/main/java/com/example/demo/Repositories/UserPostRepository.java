package com.example.demo.Repositories;

import com.example.demo.Models.Information.Country;
import com.example.demo.Models.SocialMedia.HashTag;
import com.example.demo.Models.SocialMedia.UserPost;
import com.example.demo.Models.UserManagement.Profile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

@Query("SELECT " +
        "new map(" +
        "   up.id as id, " +
        "   GROUP_CONCAT(DISTINCT CONCAT(e)) as names, " +
        "   c as country, " +
        "   GROUP_CONCAT(DISTINCT f) as mentions, " +
        "   GROUP_CONCAT(DISTINCT g) as tags, " +
        "   GROUP_CONCAT(DISTINCT b.name) as hashTags, " +
        "   up.likes as likes, " +
        "   up.shares as shares, " +
        "   up.favorites as favorites, " +
        "   up.comments as comments, " +
        "   up.caption as caption, " +
        "   up.thumbnail as thumbnail, " +
        "   up.time as timestamp, " +
        "   GROUP_CONCAT(DISTINCT CONCAT(d)) as contentTypes, " +
        "   up.path as path) " +
        "FROM UserPost up " +
        "LEFT JOIN up.country c " +
        "LEFT JOIN up.mentions f " +
        "LEFT JOIN up.tags g " +
        "LEFT JOIN up.hashTags b " +
        "LEFT JOIN up.contentTypes d " +
        "LEFT JOIN up.names e " +
        "WHERE up.id = :postId " +
        "GROUP BY up.id")
Map<String, Object> findUserPostDataById(@Param("postId") Long postId);


@Query("SELECT " +
        "new map(" +
        "   up.id as id, " +
        "   GROUP_CONCAT(DISTINCT CONCAT(e)) as names, " +
        "   c as country, " +
        "   GROUP_CONCAT(DISTINCT f) as mentions, " +
        "   GROUP_CONCAT(DISTINCT g) as tags, " +
        "   GROUP_CONCAT(DISTINCT b.name) as hashTags, " +
        "   up.likes as likes, " +
        "   up.shares as shares, " +
        "   up.favorites as favorites, " +
        "   up.comments as comments, " +
        "   up.caption as caption, " +
        "   up.thumbnail as thumbnail, " +
        "   up.time as timestamp, " +
        "   up.profile.user.username as username, " +
        "   up.profile.user.name as name, " +
        "   up.profile.user.accountType.name as accountType, " +
        "   GROUP_CONCAT(DISTINCT CONCAT(d)) as contentTypes, " +
        "   up.path as path) " +
        "FROM UserPost up " +
        "LEFT JOIN up.country c " +
        "LEFT JOIN up.mentions f " +
        "LEFT JOIN up.tags g " +
        "LEFT JOIN up.hashTags b " +
        "LEFT JOIN up.contentTypes d " +
        "LEFT JOIN up.names e " +
        "GROUP BY up.id")
List<Map<String, Object>> findUserPostData();

@Query("SELECT DISTINCT l.id FROM UserPost l WHERE  l.id = :postId")
Optional<Long> findPostIdByItsId(@Param("postId") Long postId);

}
