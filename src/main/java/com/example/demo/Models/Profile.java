package com.example.demo.Models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", nullable = false)
    User user;

    private String profilePicture;

    private String bio;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "titleId")
    private Title title;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "countryId")
    private Country country;

    @Column(nullable = false)
    private int followers;

    @Column(nullable = false)
    private int following;

    @Column(nullable = false)
    private int posts;

    @Column(nullable = false)
    private float powerSize;
}
