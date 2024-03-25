package com.example.demo.Models;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name= "countries")
@Data
@SQLDelete(sql = "UPDATE countries SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String name;

    private boolean deleted = Boolean.FALSE;

    //Below is For Bidirection relationship
    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<City> cities = new ArrayList<>();
}
