package com.example.demo.Models;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@SQLDelete(sql = "UPDATE globaldbstart_point SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class GlobalDBStartPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long hot;

    private boolean deleted = Boolean.FALSE;
}
