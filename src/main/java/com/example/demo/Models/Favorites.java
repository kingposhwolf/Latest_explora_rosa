package com.example.demo.Models;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@SQLDelete(sql = "UPDATE favorites SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class Favorites {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profileId", nullable = false, foreignKey = @ForeignKey(name = "FK_FAVORITE_PROFILE", foreignKeyDefinition = "FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE"))
    private Profile profile;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userPostId", nullable = false, foreignKey = @ForeignKey(name = "FK_FAVORITE_POST", foreignKeyDefinition = "FOREIGN KEY (user_post_id) REFERENCES user_posts(id) ON DELETE CASCADE"))
    private UserPost post;

    private boolean deleted = Boolean.FALSE;
}
