package com.example.demo.Models;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class BusinessPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "brandId")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "countryId", nullable = false)
    private Country country;

    @ManyToMany
    @JoinTable(name = "businessPostHashTag", joinColumns = @JoinColumn(name = "businessPostId"), inverseJoinColumns = @JoinColumn(name = "hashTagId", nullable = false))
    private List<HashTag> hashTags;

    private String thumbnail;

    @Column(nullable = false)
    private LocalDateTime time;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private String path;
}
