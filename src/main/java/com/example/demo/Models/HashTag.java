package com.example.demo.Models;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

/*
 * @author Dwight Danda
 *
 */
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "hashTags")
@SQLDelete(sql = "UPDATE hashTags SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class HashTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private boolean deleted = Boolean.FALSE;
}
