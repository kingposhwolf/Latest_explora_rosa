package com.example.demo.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.Models.SocialMedia.UserPost;
import com.example.demo.Models.SocialMedia.Interactions.Like;
import com.example.demo.Models.UserManagement.Profile;


public interface LikeRepository extends CrudRepository<Like, Long>{
    Optional<Like> findByLikerAndPost(Profile liker, UserPost post);

    List<Like> findByPost(UserPost post);
}
