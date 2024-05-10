package com.example.demo.Models.SocialMedia.SearchOperation;

import com.example.demo.Models.UserManagement.Profile;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@SQLDelete(sql = "UPDATE user_search_history SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class UserSearchHistory {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "search_names", joinColumns = @JoinColumn(name = "user_search_history_id"))
    @Column(name = "keywords")
    private List<String> keywords;

    @ManyToOne
    @JoinColumn(name = "profileId", nullable = false, foreignKey = @ForeignKey(name = "FK_SEARCH_HISTORY_PROFILE", foreignKeyDefinition = "FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE"))
    private Profile profile;

    @JsonIgnore
    private boolean deleted = Boolean.FALSE;
}
