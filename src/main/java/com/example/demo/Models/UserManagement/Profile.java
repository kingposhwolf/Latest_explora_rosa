package com.example.demo.Models.UserManagement;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.example.demo.Models.Ecommerce.Cart.Cart;
import com.example.demo.Models.Ecommerce.Order.Order;
import com.example.demo.Models.Information.City;
import com.example.demo.Models.Information.Country;
import com.example.demo.Models.SocialMedia.FollowUnFollow;
import com.example.demo.Models.SocialMedia.Mention;
import com.example.demo.Models.SocialMedia.UserPost;
import com.example.demo.Models.SocialMedia.Interactions.Comment;
import com.example.demo.Models.SocialMedia.Interactions.Favorites;
import com.example.demo.Models.SocialMedia.Interactions.Like;
import com.example.demo.Models.SocialMedia.Interactions.Tag;
import com.example.demo.Models.Tracking.UserToTopicTracking.TopicEngageFeedPreviousEnd;
import com.example.demo.Models.Tracking.UserToTopicTracking.TopicEngagement;
import com.example.demo.Models.Tracking.UserToUserTracking.UserEngageFeedsPreviousEnd;
import com.example.demo.Models.Tracking.UserToUserTracking.UserEngagement;
import com.example.demo.Models.UserManagement.Management.VerificationStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "profiles")
@Entity
@SQLDelete(sql = "UPDATE profiles SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "PROFILE_TYPE", discriminatorType = DiscriminatorType.STRING)
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", nullable = false, foreignKey = @ForeignKey(name = "FK__USER_PROFILE", foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"))
    User user;

    private String profilePicture;

    private String coverPhoto;

    private String bio;

    private String address;

    @ManyToOne
    @JoinColumn(name = "countryId", foreignKey = @ForeignKey(name = "FK_PROFILE_COUNTRY", foreignKeyDefinition = "FOREIGN KEY (country_id) REFERENCES countries(id) ON DELETE SET NULL"))
    private Country country;

    @Column(nullable = false)
    private VerificationStatus verificationStatus;

    @ManyToOne
    @JoinColumn(name = "cityId", foreignKey = @ForeignKey(name = "FK_PROFILE_CITY", foreignKeyDefinition = "FOREIGN KEY (city_id) REFERENCES cities(id) ON DELETE SET NULL"))
    private City city;

    @Column(nullable = false)
    private int followers;

    @Column(nullable = false)
    private int following;

    @Column(nullable = false)
    private int posts;

    @Column(nullable = false)
    private float powerSize;

    private boolean deleted = Boolean.FALSE;

    //Below is For Bidirection relationship
    @OneToMany(mappedBy = "commenter", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Favorites> favList = new ArrayList<>();

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<FollowUnFollow> followersList = new ArrayList<>();

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<FollowUnFollow> followingList = new ArrayList<>();

    @OneToMany(mappedBy = "liker", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Like> likesList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Mention> mentionList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Tag> tagList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<TopicEngageFeedPreviousEnd> topicEngageFeedPreviousEndList = new ArrayList<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<TopicEngagement> topicEngageList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UserEngageFeedsPreviousEnd> userEngageList = new ArrayList<>();

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UserEngagement> userEngageTopicList = new ArrayList<>();

    @OneToMany(mappedBy = "target", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UserEngagement> userEngageTargetList = new ArrayList<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UserPost> postList = new ArrayList<>();

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Cart cart;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Order> orderList = new ArrayList<>();
}
