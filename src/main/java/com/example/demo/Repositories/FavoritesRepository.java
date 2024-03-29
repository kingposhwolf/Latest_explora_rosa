package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.SocialMedia.UserPost;
import com.example.demo.Models.SocialMedia.Interactions.Favorites;
import com.example.demo.Models.UserManagement.Profile;

import java.util.List;


public interface FavoritesRepository extends JpaRepository<Favorites, Long>{
    List<UserPost> findUserPostsByProfile(Profile profile);
}
