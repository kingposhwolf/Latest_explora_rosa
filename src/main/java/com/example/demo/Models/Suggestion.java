package com.example.demo.Models;
/*
 * @author Dwight Danda
 *
 */
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "suggestions")
public class Suggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToMany
//    @JoinTable(
//            name = "suggestion_business_category",
//            joinColumns = @JoinColumn(name = "suggestionId"),
//            inverseJoinColumns = @JoinColumn(name = "businessCategoryId")
//    )
//    private List<BusinessCategory> businessCategories;

    @Column(nullable = false, unique = true)
    private String name;


}
