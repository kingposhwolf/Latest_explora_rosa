package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Models.SocialMedia.UserPost;
import com.example.demo.Models.SocialMedia.Interactions.Favorites;
import com.example.demo.Models.UserManagement.Profile;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface FavoritesRepository extends JpaRepository<Favorites, Long>{
    List<UserPost> findUserPostsByProfile(Profile profile);

    @Query("SELECT DISTINCT l.id FROM Favorites l WHERE  l.post.id = :postId AND l.profile.id = :profileId")
    Optional<Long> findFavoriteByPostAndUser(@Param("postId") Long postId, @Param("profileId") Long profileId);

    @Query("SELECT DISTINCT l.post.id FROM Favorites l WHERE l.profile.id = :profileId")
    List<Long> findPostByProfile(@Param("profileId") Long profileId);

    @Query("SELECT DISTINCT l.id FROM Favorites l WHERE  l.id = :favoriteId")
    Long findFavoriteByItsId(@Param("favoriteId") Long favoriteId);

    //Fetching Favorite post for a given user
    @Query("SELECT " +
         "new map(" +
         "   up.post.id as id, " +
         "   up.post.location as location, " +
         "   GROUP_CONCAT(DISTINCT CONCAT(e)) as names, " +
         "   c as country, " +
         "   GROUP_CONCAT(DISTINCT f) as mentions, " +
         "   GROUP_CONCAT(DISTINCT g) as tags, " +
         "   GROUP_CONCAT(DISTINCT b.name) as hashTags, " +
         "   up.post.likes as likes, " +
         "   up.post.shares as shares, " +
         "   up.post.favorites as favorites, " +
         "   up.post.comments as comments, " +
         "   up.post.caption as caption, " +
         "   up.post.thumbnail as thumbnail, " +
         "   up.post.time as timestamp, " +
         "   up.post.profile.id as profileId, " +
         "   up.post.profile.user.username as username, " +
         "   up.post.profile.user.name as name, " +
         "   up.post.profile.user.accountType.name as accountType, " +
         "   GROUP_CONCAT(DISTINCT CONCAT(d)) as contentTypes, " +
         "   up.post.path as path) " +
         "FROM Favorites up " +
         "LEFT JOIN up.post.country c " +
         "LEFT JOIN up.post.mentions f " +
         "LEFT JOIN up.post.tags g " +
         "LEFT JOIN up.post.hashTags b " +
         "LEFT JOIN up.post.contentTypes d " +
         "LEFT JOIN up.post.names e " +
         "WHERE up.profile.id = :profileId " +
         "GROUP BY up.post.id")
List<Map<String, Object>> findSpecificUserFavoritePost(@Param("profileId") Long profileId);

    @Query(value = "SELECT * FROM favorites WHERE user_post_id = :postId AND profile_id = :profileId", nativeQuery = true)
    Optional<Favorites> favoriteExistance(@Param("postId") Long postId, @Param("profileId") Long profileId);

}
