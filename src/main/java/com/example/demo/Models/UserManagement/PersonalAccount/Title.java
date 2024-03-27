package com.example.demo.Models.UserManagement.PersonalAccount;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "titles")
@SQLDelete(sql = "UPDATE titles SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class Title {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String name;

    private boolean deleted = Boolean.FALSE;

    //Below is For Bidirection relationship
    @OneToMany(mappedBy = "title", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Personal> personalProfilesList = new ArrayList<>();
}
