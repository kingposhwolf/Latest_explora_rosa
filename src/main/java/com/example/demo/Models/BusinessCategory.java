package com.example.demo.Models;
import java.util.List;

/*
 * @author Dwight Danda
 *
 */
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "businessCategories")
@Data
public class BusinessCategory {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @ManyToMany
    @JoinTable(name = "businessCategoryHashTag",joinColumns = @JoinColumn(name = "businessCategoryId"),inverseJoinColumns = @JoinColumn(name = "hashTagId"))
    private List<HashTag> hashTags;

}
