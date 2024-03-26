package com.example.demo.Models;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
@SQLDelete(sql = "UPDATE location SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Name or label for the location
    private String address; // Street address, city, etc.
    private Double latitude;
    private Double longitude;// We might later use it for Location.

    private boolean deleted = Boolean.FALSE;
}
