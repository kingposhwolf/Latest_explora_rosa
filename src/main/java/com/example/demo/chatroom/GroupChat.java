package com.example.demo.chatroom;

import java.util.HashSet;
import java.util.Set;

import com.example.demo.Models.UserManagement.Profile;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class GroupChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @ElementCollection
    @CollectionTable(name = "groupMembers", joinColumns = @JoinColumn(name = "group_chat_id"))
    private Set<Profile> members = new HashSet<>();
}
