package com.example.demo.Models.SocialMedia;
/*
 * @author Dwight Danda
 *
 */
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import com.example.demo.Models.Ecommerce.Cart.CartItem;
import com.example.demo.Models.Ecommerce.Order.OrderItem;
import com.example.demo.Models.Information.Country;
import com.example.demo.Models.SocialMedia.Interactions.Comment;
import com.example.demo.Models.SocialMedia.Interactions.Favorites;
import com.example.demo.Models.SocialMedia.Interactions.Like;
import com.example.demo.Models.SocialMedia.Interactions.Tag;
import com.example.demo.Models.Tracking.ViewedPosts;
import com.example.demo.Models.UserManagement.Profile;
import com.example.demo.Models.UserManagement.BussinessAccount.Brand;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@Table(name="userPosts")
@SQLDelete(sql = "UPDATE user_posts SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class UserPost {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "spotify_song_id", nullable = true)
    private String spotifySongId;

    @ElementCollection
    @CollectionTable(name = "user_post_names", joinColumns = @JoinColumn(name = "user_post_id"))
    @Column(name = "name")
    private List<String> names;

    @ManyToOne
    @JoinColumn(name = "profileId", nullable = false, foreignKey = @ForeignKey(name = "FK_USER_POST_PROFILE", foreignKeyDefinition = "FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE"))
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "countryId")
    private Country country;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "userPostHashTag", joinColumns = @JoinColumn(name = "userPostId"), inverseJoinColumns = @JoinColumn(name = "hashTagId"))
    @ToString.Exclude
    private List<HashTag> hashTags;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Mention> mentions = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "brandId")
    private Brand brand;

    @Column(nullable = false)
    private int likes;

    @Column(nullable = false)
    private int comments;

    private String caption;
    private String thumbnail;

    @Column(nullable = false)
    private LocalDateTime time;

    @ElementCollection
    @CollectionTable(name = "user_post_content_types", joinColumns = @JoinColumn(name = "user_post_id"))
    @Column(name = "contentTypes")
    private List<String> contentTypes;

    @Column(nullable = false)
    private String path;

    private int shares;
    
    private int favorites;

    private String location;

    private Long views;

    private Long duration;

    @JsonIgnore
    private boolean deleted = Boolean.FALSE;

    // Bidirectional relationships
    @OneToMany(mappedBy = "userPost", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private List<Favorites> favList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private List<Like> likesList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private List<Mention> mentionList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private List<Tag> tagList = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private List<CartItem> cartitems = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private List<OrderItem> orderList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private List<ViewedPosts> viewedPosts = new ArrayList<>();
}
