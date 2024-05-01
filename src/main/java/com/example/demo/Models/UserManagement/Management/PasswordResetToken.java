package com.example.demo.Models.UserManagement.Management;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.example.demo.Models.UserManagement.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@SQLDelete(sql = "UPDATE password_reset_token SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(nullable = false, name = "userId", foreignKey = @ForeignKey(name = "FK_PASSWORD_cHANGE_USER", foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"))
    private User user;

    private LocalDateTime expiryDate;

    @JsonIgnore
    private boolean deleted = Boolean.FALSE;
}
