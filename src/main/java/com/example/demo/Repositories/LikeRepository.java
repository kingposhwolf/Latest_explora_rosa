package com.example.demo.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.Models.SocialMedia.UserPost;
import com.example.demo.Models.SocialMedia.Interactions.Like;
import com.example.demo.Models.UserManagement.Profile;


public interface LikeRepository extends CrudRepository<Like, Long>{
    Optional<Like> findByLikerAndPost(Profile liker, UserPost post);

    List<Like> findByPost(UserPost post);

    @Query(value = "SELECT * FROM `like` WHERE user_post_id = :postId AND profile_id = :likerId AND deleted = true", nativeQuery = true)
    Optional<Like> findDeletedLikesByPostAndLiker(@Param("postId") Long postId, @Param("likerId") Long likerId);
}
