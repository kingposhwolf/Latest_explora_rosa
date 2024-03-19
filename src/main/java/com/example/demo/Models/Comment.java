package com.example.demo.Models;
/*
 * @author Dwight Danda
 *
 */
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "profileId", nullable = false, foreignKey = @ForeignKey(name = "FK_COMMENTER_PROFILE", foreignKeyDefinition = "FOREIGN KEY (profileId) REFERENCES profiles(id) ON DELETE CASCADE"))
    private Profile commenter;
    
    @ManyToOne
    @JoinColumn(name = "userPostId", nullable = false, foreignKey = @ForeignKey(name = "FK_POST_WITH_COMMENT", foreignKeyDefinition = "FOREIGN KEY (userPost_id) REFERENCES user_posts(id) ON DELETE CASCADE"))
    private UserPost userPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentCommentId")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies = new ArrayList<>();

}
