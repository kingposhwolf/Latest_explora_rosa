package com.example.demo.Repositories.SocialMedia.Content;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.example.demo.Models.Information.Country;
import com.example.demo.Models.SocialMedia.HashTag;
import com.example.demo.Models.SocialMedia.UserPost;
import com.example.demo.Models.UserManagement.Profile;


@Repository
public interface UserPostRepository extends JpaRepository<UserPost, Long> {

        @Query("SELECT up FROM UserPost up WHERE up.id >= :startId AND :hashTag MEMBER OF up.hashTags AND up.likes > :likesThreshold")
        List<UserPost> findByHashTagsAndLikesGreaterThanStartingFromId(
                        @Param("startId") Long startId,
                        @Param("hashTag") HashTag hashTag,
                        @Param("likesThreshold") int likesThreshold,
                        Pageable pageable);

        @Query("SELECT up FROM UserPost up WHERE up.id >= :startId AND up.profile IN :profiles AND up.likes > :likesThreshold")
        List<UserPost> findByProfilesAndLikesGreaterThanStartingFromId(
                        @Param("startId") Long startId,
                        @Param("profiles") List<Profile> profiles,
                        @Param("likesThreshold") int likesThreshold,
                        Pageable pageable);

        // Returns posts based on Hashtags and higher likes
        List<UserPost> findByHashTagsInAndLikesGreaterThanAndIdGreaterThan(List<HashTag> hashTags, int likesThreshold,
                        Long startId, Pageable pageable);

        // Returns post based on given profiles and higher likes
        List<UserPost> findByProfileInAndLikesGreaterThanAndIdGreaterThan(List<Profile> profiles, int likesThreshold,
                        Long startId, Pageable pageable);

        // Return the tranding post for a given Country
        List<UserPost> findByCountryAndLikesGreaterThanAndIdGreaterThan(Country country, int likesThreshold,
                        Long startId, Pageable pageable);

        // Used in search Algorithm to search the given keyword on the HashTag, with
        // bigger likes
        @Query("SELECT DISTINCT up FROM UserPost up " + "JOIN up.hashTags ht "
                        + "WHERE LOWER(ht.name) LIKE LOWER(concat('%', :keyword, '%')) " +
                        "AND up.likes = (SELECT MAX(up2.likes) FROM UserPost up2 " + "JOIN up2.hashTags ht2 " +
                        "WHERE LOWER(ht2.name) LIKE LOWER(concat('%', :keyword, '%')))")
        List<UserPost> findByHashTagsContainingKeywordAndMaxLikes(@Param("keyword") String keyword, Pageable pageable);

        @Query("SELECT DISTINCT up FROM UserPost up " + "JOIN up.hashTags ht " + "WHERE ht.name = :keyword " +
                        "AND up.likes = (SELECT MAX(up2.likes) FROM UserPost up2 "
                        + "JOIN up2.hashTags ht2 WHERE ht2.name = :keyword)")
        List<UserPost> findByHashTagsMatchKeywordAndMaxLikes(@Param("keyword") String keyword, Pageable pageable);

        // @Query("SELECT " +
        //                 "new map(" +
        //                 "   up.id as id, " +
        //                 "   GROUP_CONCAT(DISTINCT CONCAT(e)) as names, " +
        //                 "   c as country, " +
        //                 "   GROUP_CONCAT(DISTINCT f) as mentions, " +
        //                 "   GROUP_CONCAT(DISTINCT g) as tags, " +
        //                 "   GROUP_CONCAT(DISTINCT b.name) as hashTags, " +
        //                 "   up.likes as likes, " +
        //                 "   up.shares as shares, " +
        //                 "   up.favorites as favorites, " +
        //                 "   up.comments as comments, " +
        //                 "   up.caption as caption, " +
        //                 "   up.thumbnail as thumbnail, " +
        //                 "   up.time as timestamp, " +
        //                 "   up.profile.user.username as username, " +
        //                 "   up.profile.user.name as name, " +
        //                 "   up.profile.user.accountType.name as accountType, " +
        //                 "   GROUP_CONCAT(DISTINCT CONCAT(d)) as contentTypes, " +
        //                 "   up.path as path) " +
        //                 "FROM UserPost up " +
        //                 "LEFT JOIN up.country c " +
        //                 "LEFT JOIN up.mentions f " +
        //                 "LEFT JOIN up.tags g " +
        //                 "LEFT JOIN up.hashTags b " +
        //                 "LEFT JOIN up.contentTypes d " +
        //                 "LEFT JOIN up.names e " +
        //                 "WHERE up.id = :postId " +
        //                 "GROUP BY up.id")
        // Map<String, Object> findUserPostDataById(@Param("postId") Long postId);

        @Query(value =
        "SELECT p.id as id, " +
        "p.location as location, " +
        "   GROUP_CONCAT(DISTINCT CONCAT(e.name)) as names, " +
        "   c.name as country, " +
        "   GROUP_CONCAT(DISTINCT f.profile_id) as mentions, " +
        "   GROUP_CONCAT(DISTINCT g.profile_id) as tags, " +
        "   GROUP_CONCAT(DISTINCT h.name) as hashTags, " +
        "   p.likes as likes, " +
        "   p.shares as shares, " +
        "   p.favorites as favorites, " +
        "   p.comments as comments, " +
        "   p.caption as caption, " +
        "   p.thumbnail as thumbnail, " +
        "   p.time as timestamp, " +
        "   p.profile_id as profileId, " +
        "   us.username as username, " +
        "   us.name as name, " +
        "   acc.name as accountType, " +
        "   GROUP_CONCAT(DISTINCT CONCAT(d.content_types)) as contentTypes, " +
        "   p.path as path " +
        "FROM user_posts p " +
        "JOIN user_post_hash_tag ph ON p.id = ph.user_post_id " +
        "JOIN hash_tags h ON ph.hash_tag_id = h.id " +
        "LEFT JOIN countries c ON p.country_id = c.id " +
        "LEFT JOIN mention f ON p.id = f.user_post_id " +
        "LEFT JOIN tag g ON p.id = g.user_post_id " +
        "LEFT JOIN user_post_content_types d ON p.id = d.user_post_id " +
        "LEFT JOIN user_post_names e ON p.id = e.user_post_id " +
        "LEFT JOIN profiles pr ON p.profile_id = pr.id " +
        "LEFT JOIN users us ON pr.user_id = us.id " +
        "LEFT JOIN account_type acc ON us.account_type_id = acc.id " +
        "WHERE p.id = :postId " +
        "GROUP BY p.id",
        nativeQuery = true)
        Map<String, Object> findUserPostDataById(@Param("postId") Long postId);

        // List all posts.
        // @Query("SELECT " +
        //                 "new map(" +
        //                 "   up.id as id, " +
        //                 "   up.location as location, " +
        //                 "   GROUP_CONCAT(DISTINCT CONCAT(e)) as names, " +
        //                 "   c as country, " +
        //                 "   GROUP_CONCAT(DISTINCT f) as mentions, " +
        //                 "   GROUP_CONCAT(DISTINCT g) as tags, " +
        //                 "   GROUP_CONCAT(DISTINCT b.name) as hashTags, " +
        //                 "   up.likes as likes, " +
        //                 "   up.shares as shares, " +
        //                 "   up.favorites as favorites, " +
        //                 "   up.comments as comments, " +
        //                 "   up.caption as caption, " +
        //                 "   up.thumbnail as thumbnail, " +
        //                 "   up.time as timestamp, " +
        //                 "   up.profile.id as profileId, " +
        //                 "   up.profile.user.username as username, " +
        //                 "   up.profile.user.name as name, " +
        //                 "   up.profile.user.accountType.name as accountType, " +
        //                 "   GROUP_CONCAT(DISTINCT CONCAT(d)) as contentTypes, " +
        //                 "   up.path as path) " +
        //                 "FROM UserPost up " +
        //                 "LEFT JOIN up.country c " +
        //                 "LEFT JOIN up.mentions f " +
        //                 "LEFT JOIN up.tags g " +
        //                 "LEFT JOIN up.hashTags b " +
        //                 "LEFT JOIN up.contentTypes d " +
        //                 "LEFT JOIN up.names e " +
        //                 "GROUP BY up.id")
        // List<Map<String, Object>> findUserPostData();

        // @Query(value =
        // "SELECT p.id as id, " +
        // "p.location as location, " +
        // "   GROUP_CONCAT(DISTINCT CONCAT(e.name)) as names, " +
        // "   c.name as country, " +
        // "   GROUP_CONCAT(DISTINCT f.profile_id) as mentions, " +
        // "   GROUP_CONCAT(DISTINCT g.profile_id) as tags, " +
        // "   GROUP_CONCAT(DISTINCT h.name) as hashTags, " +
        // "   p.likes as likes, " +
        // "   p.shares as shares, " +
        // "   p.favorites as favorites, " +
        // "   p.comments as comments, " +
        // "   p.caption as caption, " +
        // "   p.thumbnail as thumbnail, " +
        // "   p.time as timestamp, " +
        // "   p.profile_id as profileId, " +
        // "   us.username as username, " +
        // "   us.name as name, " +
        // "   acc.name as accountType, " +
        // "   GROUP_CONCAT(DISTINCT CONCAT(d.content_types)) as contentTypes, " +
        // "   p.path as path " +
        // "FROM user_posts p " +
        // "JOIN user_post_hash_tag ph ON p.id = ph.user_post_id " +
        // "JOIN hash_tags h ON ph.hash_tag_id = h.id " +
        // "LEFT JOIN countries c ON p.country_id = c.id " +
        // "LEFT JOIN mention f ON p.id = f.user_post_id " +
        // "LEFT JOIN tag g ON p.id = g.user_post_id " +
        // "LEFT JOIN user_post_content_types d ON p.id = d.user_post_id " +
        // "LEFT JOIN user_post_names e ON p.id = e.user_post_id " +
        // "LEFT JOIN profiles pr ON p.profile_id = pr.id " +
        // "LEFT JOIN users us ON pr.user_id = us.id " +
        // "LEFT JOIN account_type acc ON us.account_type_id = acc.id " +
        // "GROUP BY p.id",
        // nativeQuery = true)
        // List<Map<String, Object>> findUserPostData();

        @Query(value =
            "SELECT p.id as id, " +
            "p.location as location, " +
            "   GROUP_CONCAT(DISTINCT CONCAT(e.name)) as names, " +
            "   c.name as country, " +
            "   GROUP_CONCAT(DISTINCT f.profile_id) as mentions, " +
            "   GROUP_CONCAT(DISTINCT g.profile_id) as tags, " +
            "   GROUP_CONCAT(DISTINCT h.name) as hashTags, " +
            "   p.likes as likes, " +
            "   p.shares as shares, " +
            "   p.favorites as favorites, " +
            "   p.comments as comments, " +
            "   p.caption as caption, " +
            "   p.thumbnail as thumbnail, " +
            "   p.time as timestamp, " +
            "   p.profile_id as profileId, " +
            "   us.username as username, " +
            "   us.name as name, " +
            "   acc.name as accountType, " +
            "   GROUP_CONCAT(DISTINCT CONCAT(d.content_types)) as contentTypes, " +
            "   p.path as path " +
            "FROM user_posts p " +
            "JOIN user_post_hash_tag ph ON p.id = ph.user_post_id " +
            "JOIN hash_tags h ON ph.hash_tag_id = h.id " +
            "LEFT JOIN countries c ON p.country_id = c.id " +
            "LEFT JOIN mention f ON p.id = f.user_post_id " +
            "LEFT JOIN tag g ON p.id = g.user_post_id " +
            "LEFT JOIN user_post_content_types d ON p.id = d.user_post_id " +
            "LEFT JOIN user_post_names e ON p.id = e.user_post_id " +
            "LEFT JOIN profiles pr ON p.profile_id = pr.id " +
            "LEFT JOIN users us ON pr.user_id = us.id " +
            "LEFT JOIN account_type acc ON us.account_type_id = acc.id " +
            "GROUP BY p.id " +
            "ORDER BY RAND(:seed) " +  // Order by RAND with seed
            "LIMIT :pageSize OFFSET :offset",  // Apply pagination
            nativeQuery = true)
    List<Map<String, Object>> findUserPostData(
            @Param("seed") long seed,  // Pass seed as parameter
            @Param("pageSize") int pageSize,
            @Param("offset") int offset);


        //Return the id of the post id only
        @Query("SELECT DISTINCT l.id FROM UserPost l WHERE  l.id = :postId")
        Optional<Long> findPostIdByItsId(@Param("postId") Long postId);

         // List all posts.
        // @Query("SELECT " +
        // "new map(" +
        // "   up.id as id, " +
        // "   up.location as location, " +
        // "   GROUP_CONCAT(DISTINCT CONCAT(e)) as names, " +
        // "   c as country, " +
        // "   GROUP_CONCAT(DISTINCT f) as mentions, " +
        // "   GROUP_CONCAT(DISTINCT g) as tags, " +
        // "   GROUP_CONCAT(DISTINCT b.name) as hashTags, " +
        // "   up.likes as likes, " +
        // "   up.shares as shares, " +
        // "   up.favorites as favorites, " +
        // "   up.comments as comments, " +
        // "   up.caption as caption, " +
        // "   up.thumbnail as thumbnail, " +
        // "   up.time as timestamp, " +
        // "   up.profile.id as profileId, " +
        // "   up.profile.user.username as username, " +
        // "   up.profile.user.name as name, " +
        // "   up.profile.user.accountType.name as accountType, " +
        // "   GROUP_CONCAT(DISTINCT CONCAT(d)) as contentTypes, " +
        // "   up.path as path) " +
        // "FROM UserPost up " +
        // "LEFT JOIN up.country c " +
        // "LEFT JOIN up.mentions f " +
        // "LEFT JOIN up.tags g " +
        // "LEFT JOIN up.hashTags b " +
        // "LEFT JOIN up.contentTypes d " +
        // "LEFT JOIN up.names e " +
        // "WHERE up.profile.id = :profileId " +
        // "GROUP BY up.id")
        // List<Map<String, Object>> findSpecificUserPostData(@Param("profileId") Long profileId);

        @Query(value =
        "SELECT p.id as id, " +
        "p.location as location, " +
        "   GROUP_CONCAT(DISTINCT CONCAT(e.name)) as names, " +
        "   c.name as country, " +
        "   GROUP_CONCAT(DISTINCT f.profile_id) as mentions, " +
        "   GROUP_CONCAT(DISTINCT g.profile_id) as tags, " +
        "   GROUP_CONCAT(DISTINCT h.name) as hashTags, " +
        "   p.likes as likes, " +
        "   p.shares as shares, " +
        "   p.favorites as favorites, " +
        "   p.comments as comments, " +
        "   p.caption as caption, " +
        "   p.thumbnail as thumbnail, " +
        "   p.time as timestamp, " +
        "   p.profile_id as profileId, " +
        "   us.username as username, " +
        "   us.name as name, " +
        "   acc.name as accountType, " +
        "   GROUP_CONCAT(DISTINCT CONCAT(d.content_types)) as contentTypes, " +
        "   p.path as path " +
        "FROM user_posts p " +
        "JOIN user_post_hash_tag ph ON p.id = ph.user_post_id " +
        "JOIN hash_tags h ON ph.hash_tag_id = h.id " +
        "LEFT JOIN countries c ON p.country_id = c.id " +
        "LEFT JOIN mention f ON p.id = f.user_post_id " +
        "LEFT JOIN tag g ON p.id = g.user_post_id " +
        "LEFT JOIN user_post_content_types d ON p.id = d.user_post_id " +
        "LEFT JOIN user_post_names e ON p.id = e.user_post_id " +
        "LEFT JOIN profiles pr ON p.profile_id = pr.id " +
        "LEFT JOIN users us ON pr.user_id = us.id " +
        "LEFT JOIN account_type acc ON us.account_type_id = acc.id " +
        "WHERE p.profile_id = :profileId " +
        "GROUP BY p.id",
        nativeQuery = true)
        List<Map<String, Object>> findSpecificUserPostData(@Param("profileId") Long profileId);


        @Query(value =
        "SELECT p.id as id, " +
        "p.location as location, " +
        "   GROUP_CONCAT(DISTINCT CONCAT(e.name)) as names, " +
        "   c.name as country, " +
        "   GROUP_CONCAT(DISTINCT f.profile_id) as mentions, " +
        "   GROUP_CONCAT(DISTINCT g.profile_id) as tags, " +
        "   GROUP_CONCAT(DISTINCT h.name) as hashTags, " +
        "   p.likes as likes, " +
        "   p.shares as shares, " +
        "   p.favorites as favorites, " +
        "   p.comments as comments, " +
        "   p.caption as caption, " +
        "   p.thumbnail as thumbnail, " +
        "   p.time as timestamp, " +
        "   p.profile_id as profileId, " +
        "   us.username as username, " +
        "   us.name as name, " +
        "   acc.name as accountType, " +
        "   GROUP_CONCAT(DISTINCT CONCAT(d.content_types)) as contentTypes, " +
        "   p.path as path " +
        "FROM user_posts p " +
        "JOIN user_post_hash_tag ph ON p.id = ph.user_post_id " +
        "JOIN hash_tags h ON ph.hash_tag_id = h.id " +
        "LEFT JOIN countries c ON p.country_id = c.id " +
        "LEFT JOIN mention f ON p.id = f.user_post_id " +
        "LEFT JOIN tag g ON p.id = g.user_post_id " +
        "LEFT JOIN user_post_content_types d ON p.id = d.user_post_id " +
        "LEFT JOIN user_post_names e ON p.id = e.user_post_id " +
        "LEFT JOIN profiles pr ON p.profile_id = pr.id " +
        "LEFT JOIN users us ON pr.user_id = us.id " +
        "LEFT JOIN account_type acc ON us.account_type_id = acc.id " +
        "WHERE p.id IN (" +
        "   SELECT DISTINCT p.id " +
        "   FROM user_posts p " +
        "   JOIN user_post_hash_tag ph ON p.id = ph.user_post_id " +
        "   JOIN hash_tags h ON ph.hash_tag_id = h.id " +
        "WHERE LEVENSHTEIN(h.name, :keyword) <= 2 OR h.name LIKE CONCAT('%', :keyword, '%') " +
        ") " +
        "GROUP BY p.id",
        nativeQuery = true)
        List<Map<String, Object>> searchOnHashTag(@Param("keyword") String keyword);


}
