package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.Favorites;
import com.example.demo.Models.Profile;
import com.example.demo.Models.UserPost;

import java.util.List;


public interface FavoritesRepository extends JpaRepository<Favorites, Long>{
    List<UserPost> findUserPostsByProfile(Profile profile);
}
