package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Models.SocialMedia.UserPost;
import com.example.demo.Models.SocialMedia.Interactions.Favorites;
import com.example.demo.Models.UserManagement.Profile;

import java.util.List;


public interface FavoritesRepository extends JpaRepository<Favorites, Long>{
    List<UserPost> findUserPostsByProfile(Profile profile);

    @Query("SELECT DISTINCT l.id FROM Favorites l WHERE  l.post.id = :postId AND l.profile.id = :profileId")
    Long findFavoriteByPostAndUser(@Param("postId") Long postId, @Param("profileId") Long profileId);

    @Query("SELECT DISTINCT l.post.id FROM Favorites l WHERE l.profile.id = :profileId")
    List<Long> findPostByProfile(@Param("profileId") Long profileId);

    @Query("SELECT DISTINCT l.id FROM Favorites l WHERE  l.id = :favoriteId")
    Long findFavoriteByItsId(@Param("favoriteId") Long favoriteId);
}
