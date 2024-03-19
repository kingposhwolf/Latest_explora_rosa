/*
 * @author Dwight Danda
 *
 */
package com.example.demo.Models;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="accountType")
@Data
@SQLDelete(sql = "UPDATE account_type SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class AccountType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String name;

    private boolean deleted = Boolean.FALSE;
}
