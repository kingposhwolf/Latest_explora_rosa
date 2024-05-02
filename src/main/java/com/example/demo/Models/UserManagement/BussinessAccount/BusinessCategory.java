package com.example.demo.Models.UserManagement.BussinessAccount;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.fasterxml.jackson.annotation.JsonIgnore;

/*
 * @author Dwight Danda
 *
 */
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "businessCategories")
@Data
@SQLDelete(sql = "UPDATE business_categories SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class BusinessCategory {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @JsonIgnore
    private boolean deleted = Boolean.FALSE;
}
