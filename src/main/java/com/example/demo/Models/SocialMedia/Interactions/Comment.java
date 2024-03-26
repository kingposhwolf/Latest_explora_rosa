package com.example.demo.Models;
/*
 * @author Dwight Danda
 *
 */
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "comments")
@SQLDelete(sql = "UPDATE comments SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "profileId", nullable = false, foreignKey = @ForeignKey(name = "FK_COMMENTER_PROFILE", foreignKeyDefinition = "FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE"))
    private Profile commenter;
    
    @ManyToOne
    @JoinColumn(name = "userPostId", nullable = false, foreignKey = @ForeignKey(name = "FK_POST_WITH_COMMENT", foreignKeyDefinition = "FOREIGN KEY (user_post_id) REFERENCES user_posts(id) ON DELETE CASCADE"))
    private UserPost userPost;

    @ManyToOne
    @JoinColumn(name = "parentCommentId", foreignKey = @ForeignKey(name = "FK_PARENT_COMMENT", foreignKeyDefinition = "FOREIGN KEY (parent_comment_id) REFERENCES comments(id) ON DELETE CASCADE"))
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies = new ArrayList<>();

    private boolean deleted = Boolean.FALSE;
}
