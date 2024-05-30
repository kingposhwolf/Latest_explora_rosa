package com.example.demo.Repositories.SocialMedia.Content;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Models.SocialMedia.BusinessPost;

public interface BusinessPostRepository extends JpaRepository<BusinessPost, Long> {
    @Query(value =
        "SELECT p.id as id, " +
        "p.location as location, " +
        "   GROUP_CONCAT(DISTINCT CONCAT(e.name)) as names, " +
        "   c.name as country, " +
        "   GROUP_CONCAT(DISTINCT f.profile_id) as mentions, " +
        "   GROUP_CONCAT(DISTINCT g.profile_id) as tags, " +
        "   GROUP_CONCAT(DISTINCT CONCAT(h.id, ':', h.name)) as hashTags, " +
        "   p.likes as likes, " +
        "   p.shares as shares, " +
        "   p.favorites as favorites, " +
        "   p.comments as comments, " +
        "   p.caption as caption, " +
        "   pr.verification_status as verification_status, " +
        "   p.thumbnail as thumbnail, " +
        "   p.views as views, " +
        "   p.time as timestamp, " +
        "   k.rate as rate, " +
        "   k.price as price, " +
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
        "LEFT JOIN business_post k ON p.id = k.id " +
        "LEFT JOIN tag g ON p.id = g.user_post_id " +
        "LEFT JOIN user_post_content_types d ON p.id = d.user_post_id " +
        "LEFT JOIN user_post_names e ON p.id = e.user_post_id " +
        "LEFT JOIN profiles pr ON p.profile_id = pr.id " +
        "LEFT JOIN users us ON pr.user_id = us.id " +
        "LEFT JOIN account_type acc ON us.account_type_id = acc.id " +
        "WHERE p.id = :postId " +
        "GROUP BY p.id",
        nativeQuery = true)
        Map<String, Object> findBusinessPostDataById(@Param("postId") Long postId);

        @Query("SELECT l.id  FROM BusinessPost l  WHERE l.id = :postId ")
        Optional<Long> getBusinessPostIdByItsId(@Param("postId") Long postId);
}
