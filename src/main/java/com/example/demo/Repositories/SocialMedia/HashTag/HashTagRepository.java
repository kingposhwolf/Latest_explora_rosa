package com.example.demo.Repositories.SocialMedia.HashTag;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.SocialMedia.HashTag;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface HashTagRepository extends CrudRepository<HashTag, Long> {
    Optional<HashTag> findByName(String name);

    List<HashTag> findAllByIdIn(List<Long> hashTagIds);

    @Query(value =
        "SELECT h.id AS hashTagId,h.name AS hashtag, upht.user_post_id AS posts " +
        "FROM hash_tags h " +
        "JOIN user_post_hash_tag upht ON h.id = upht.hash_tag_id " +
        "WHERE h.deleted = FALSE " +
        "AND LEVENSHTEIN(h.name, :keyword) <= 2 OR h.name LIKE CONCAT('%', :keyword, '%') ",
        nativeQuery = true)
    List<Map<String, Object>> findActiveHashTagsWithPosts(@Param("keyword") String keyword);

    @Query(value =
        "SELECT p.id as id, " +
        "   p.location as location, " +
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
        "   p.thumbnail as thumbnail, " +
        "   p.time as timestamp, " +
        "   p.profile_id as profileId, " +
        "   pr.verification_status as verification_status, " +
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
        "WHERE h.id = :id " +
        ") " +
        "GROUP BY p.id",
        nativeQuery = true)
        List<Map<String, Object>> taggedPost(@Param("id") Long id);

}
